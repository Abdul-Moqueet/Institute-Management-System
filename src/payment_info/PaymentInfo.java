package payment_info;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import my_utils.MyAnimations;
import my_utils.MyDatabase;
import my_utils.MyUtils;
import take_admission.StudentModal;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PaymentInfo implements Initializable {

    @FXML
    private Pane main_pane, pane_pay;

    @FXML
    private TableView<StudentModal> table_view;

    @FXML
    private TableColumn<StudentModal, String> name, sClass, batch, course, pay_date, doa;

    @FXML
    private TableColumn<StudentModal, Integer> total_fee, dues, paid;

    @FXML
    private JFXTextField tf_total_fee, tf_dues, tf_pay, tf_search;

    @FXML
    private ImageView img_v_pic;

    @FXML
    private Label lb_name, lb_pay_error;

    @FXML
    private JFXButton btn_pay;

    @FXML
    private JFXComboBox<String> cb_by, cb_batch, cb_stats;

    private int prevPayAmt, id, prevDues;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        main_pane.setTranslateX(1166);
        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(main_pane, 0.5, 1166, 0, null));

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        sClass.setCellValueFactory(new PropertyValueFactory<>("s_class"));
        batch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        course.setCellValueFactory(new PropertyValueFactory<>("course"));
        total_fee.setCellValueFactory(new PropertyValueFactory<>("total_fee"));
        dues.setCellValueFactory(new PropertyValueFactory<>("dues"));
        paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        pay_date.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
        doa.setCellValueFactory(new PropertyValueFactory<>("doa"));

        MyDatabase.refreshMasterDataObservableList();
        FilteredList<StudentModal> filteredData = new FilteredList<>(MyDatabase.masterDataObservableList, p -> true);

        SortedList<StudentModal> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        table_view.setItems(sortedData);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                lb_name.setText(observable.getValue().getName());
                tf_total_fee.setText(String.valueOf(observable.getValue().getTotal_fee()));
                tf_dues.setText(String.valueOf(observable.getValue().getDues()));
                prevPayAmt = observable.getValue().getPaid();
                prevDues = observable.getValue().getDues();
                id = observable.getValue().getId();

                if (observable.getValue().getPic().equalsIgnoreCase("default_avatar.png")) {
                    img_v_pic.setImage(new Image(getClass().getResourceAsStream("default_avatar.png")));
                } else {
                    try {
                        img_v_pic.setImage(new Image(new FileInputStream(observable.getValue().getPic())));
                    } catch (IOException e) {
                        img_v_pic.setImage(new Image(getClass().getResourceAsStream("default_avatar.png")));
                    }
                }
            } catch (Exception ignored) { }

            if(prevDues==0)
                btn_pay.setDisable(true);
            else
                btn_pay.setDisable(false);

        });

        MyUtils.setNumberOnly(tf_pay);
        cb_stats.getItems().addAll("All", "Paid", "Unpaid");
        cb_stats.setValue("All");
        cb_stats.setOnAction(e->search(filteredData, tf_search.getText()));

        cb_by.getItems().addAll("Name", "Class", "Course");
        cb_by.setValue("Name");
        cb_by.setOnAction(e->search(filteredData, tf_search.getText()));

        cb_batch.getItems().addAll("All", "08:00 AM", "11:00 AM", "04:00 PM");
        cb_batch.setValue("All");
        cb_batch.setOnAction(e->search(filteredData, tf_search.getText()));

        btn_pay.setOnAction(e -> pay());

        tf_pay.textProperty().addListener((observable, oldValue, newValue) -> {

            int pay = 0;
            if (tf_pay.getText().isEmpty())
                tf_dues.setText(String.valueOf(prevDues));
            else {
                pay = Integer.parseInt(newValue);
                tf_dues.setText(String.valueOf(prevDues - pay));
            }
            if (pay > prevDues)
                tf_pay.setText(String.valueOf(prevDues));

        });

        tf_pay.setOnAction(e->pay());
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> search(filteredData, newValue));

    }

    private void search(FilteredList<StudentModal> filteredData, String newValue) {
        filteredData.setPredicate(s -> {

            int batchIndex = cb_batch.getSelectionModel().getSelectedIndex();
            int statsIndex = cb_stats.getSelectionModel().getSelectedIndex();

            if ((newValue == null || newValue.isEmpty())) {
                return true;
            }

            String targetText = newValue.toLowerCase();
            String targetColumn;

            switch (cb_by.getSelectionModel().getSelectedIndex()) {
                case 1:
                    targetColumn = s.getS_class().toLowerCase();
                    break;
                case 2:
                    targetColumn = s.getCourse().toLowerCase();
                    break;
                default:
                    targetColumn = s.getName().toLowerCase();
            }


            if(batchIndex==0){

                if(statsIndex==0){
                    if (targetColumn.contains(targetText))
                        return true;
                }else{

                    if(statsIndex==1){
                        //Paid
                        if (s.getDues()==0 && targetColumn.contains(targetText))
                            return true;
                    }else{
                        //Unpaid
                        if (s.getDues()>0 && targetColumn.contains(targetText))
                            return true;

                    }

                }

            }else{

                if(statsIndex==0){
                    if (cb_batch.getValue().equals(s.getBatch()) && targetColumn.contains(targetText))
                        return true;
                }else {

                    if(statsIndex==1){
                        //Paid
                        if (cb_batch.getValue().equals(s.getBatch()) && s.getDues()==0 && targetColumn.contains(targetText))
                            return true;
                    }else{
                        //Unpaid
                        if (cb_batch.getValue().equals(s.getBatch()) && s.getDues()>0 && targetColumn.contains(targetText))
                            return true;
                    }

                }

            }

            return false;

        });
    }

    private void pay() {

        if (tf_pay.getText().trim().isEmpty()) {
            invalidAmount(tf_pay, lb_pay_error, pane_pay, "Can't be empty");
            return;
        }

        lb_pay_error.setStyle("-fx-opacity: 0");

        int paying = Integer.parseInt(tf_pay.getText());

        if (paying == 0) {
            invalidAmount(tf_pay, lb_pay_error, pane_pay, "Amount Can't be 0");
            tf_pay.clear();
            return;
        }

        lb_pay_error.setStyle("-fx-opacity: 0");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (MyDatabase.payFee(prevPayAmt + paying, prevDues - paying, date, id)) {
            refreshTable(prevPayAmt + paying, prevDues - paying, date);
            MyUtils.showSnackbar(main_pane, "Paid Rs " + paying + " Successfully", 3);
            tf_pay.clear();
        }

    }

    private void refreshTable(int paid, int dues, String date) {
        StudentModal studentModal = table_view.getSelectionModel().getSelectedItem();
        studentModal.setPaid(paid);
        studentModal.setDues(dues);
        studentModal.setPay_date(date);
        prevDues = dues;
        if(prevDues==0)
            btn_pay.setDisable(true);
        table_view.refresh();

    }

    public static void invalidAmount(TextField tf, Label lb, Pane pane, String msg) {
        lb.setText(msg);
        lb.setStyle("-fx-opacity: 1");
        MyAnimations.shake(pane, 0.08, 0, 10, 6, null);
        tf.requestFocus();
    }

}
