package com.abdul_moqueet.student_info;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import com.abdul_moqueet.my_alert.MyAlert;
import com.abdul_moqueet.my_utils.MyAnimations;
import com.abdul_moqueet.my_utils.MyDatabase;
import com.abdul_moqueet.my_utils.MyUtils;
import com.abdul_moqueet.take_admission.StudentModal;
import com.abdul_moqueet.take_admission.TakeAdmission;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.abdul_moqueet.my_utils.MyUtils.isNotEmpty;

public class StudentInfo implements Initializable {

    @FXML
    private Pane main_pane, pane_name, pane_f_name, pane_phone, pane_address;

    @FXML
    private TableView<StudentModal> table_view;

    @FXML
    private TableColumn<StudentModal, String> name, sClass, gender, batch, course, doa;

    @FXML
    private JFXComboBox<String> cb_class, cb_gender, cb_course, cb_batch, cb_by, cb_by_batch;

    @FXML
    private JFXTextField tf_name, tf_f_name, tf_phone, tf_address, tf_search;

    @FXML
    private Label lb_name_error, lb_f_name_error, lb_phone_error, lb_address_error;

    @FXML
    private JFXDatePicker dp_doa;

    @FXML
    private ImageView img_v_pic;

    @FXML
    private JFXButton btn_update_pic, btn_save, btn_delete;

    private String picPath = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        main_pane.setTranslateX(1166);
        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(main_pane, 0.5, 1166, 0, null));

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        sClass.setCellValueFactory(new PropertyValueFactory<>("s_class"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        batch.setCellValueFactory(new PropertyValueFactory<>("batch"));
        course.setCellValueFactory(new PropertyValueFactory<>("course"));
        doa.setCellValueFactory(new PropertyValueFactory<>("doa"));

        MyDatabase.refreshMasterDataObservableList();
        FilteredList<StudentModal> filteredData = new FilteredList<>(MyDatabase.masterDataObservableList, p -> true);

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> search(filteredData, newValue));

        SortedList<StudentModal> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        table_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        table_view.setItems(sortedData);

        cb_course.getItems().addAll("C-Language", "C++", "Java", "JavaScript", "Python");
        cb_course.setValue(cb_course.getItems().get(0));

        cb_batch.getItems().addAll("08:00 AM", "11:00 AM", "04:00 PM");
        cb_batch.setValue(cb_batch.getItems().get(0));

        cb_class.getItems().addAll("BCA-I", "BCA-II", "BCA-III", "MCA-I", "MCA-II", "MCA-III");
        cb_class.setValue(cb_class.getItems().get(0));

        cb_gender.getItems().addAll("Male", "Female");
        cb_gender.setValue(cb_gender.getItems().get(0));

        cb_by_batch.getItems().addAll("All", "08:00 AM", "11:00 AM", "04:00 PM");
        cb_by_batch.setValue(cb_by_batch.getItems().get(0));
        cb_by_batch.setOnAction(e->search(filteredData, tf_search.getText()));

        cb_by.getItems().addAll("Name", "Class", "Course");
        cb_by.setValue(cb_by.getItems().get(0));
        cb_by.setOnAction(e->search(filteredData, tf_search.getText()));

        MyUtils.dateConverter(dp_doa);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                tf_name.setText(observable.getValue().getName());
                cb_class.setValue(observable.getValue().getS_class());
                cb_gender.setValue(observable.getValue().getGender());
                tf_f_name.setText(observable.getValue().getF_name());
                tf_phone.setText(observable.getValue().getPhone());
                dp_doa.setValue(LocalDate.parse(observable.getValue().getDoa(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                cb_batch.setValue(observable.getValue().getBatch());
                cb_course.setValue(observable.getValue().getCourse());
                tf_address.setText(observable.getValue().getAddress());

                if (observable.getValue().getPic().equalsIgnoreCase("default_avatar.png")) {
                    img_v_pic.setImage(new Image(getClass().getResourceAsStream("default_avatar.png")));
                } else {
                    try {
                        img_v_pic.setImage(new Image(new FileInputStream(observable.getValue().getPic())));
                    } catch (IOException e) {
                        img_v_pic.setImage(new Image(getClass().getResourceAsStream("default_avatar.png")));
                    }
                }
            }catch (Exception ignored){ }

        });

        btn_save.setOnAction(e->saveData());
        btn_update_pic.setOnAction(e -> picPath = new TakeAdmission().chooseFile(e, img_v_pic));
        btn_delete.setOnAction(e->delete());

    }

    private void search(FilteredList<StudentModal> filteredData, String newValue) {
        filteredData.setPredicate(s -> {

            int index = cb_by_batch.getSelectionModel().getSelectedIndex();

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

            if (index == 0) {

                if (targetColumn.contains(targetText))
                    return true;
            } else {

                if (cb_by_batch.getValue().equals(s.getBatch()) && targetColumn.contains(targetText))
                    return true;

            }

            return false;

        });
    }

    private void saveData() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        if (isNotEmpty(tf_name, lb_name_error, pane_name) && isNotEmpty(tf_phone, lb_phone_error, pane_phone) &&
                isNotEmpty(tf_f_name, lb_f_name_error, pane_f_name) && isNotEmpty(tf_address, lb_address_error, pane_address)) {

            if(MyUtils.checkPhoneNo(tf_phone.getText(), lb_phone_error, pane_phone)){

                String doa = dp_doa.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                int id = table_view.getSelectionModel().getSelectedItem().getId();

                if (picPath.matches(""))
                    picPath = table_view.getSelectionModel().getSelectedItem().getPic();

                StudentModal studentModal = new StudentModal(id, tf_name.getText(), cb_class.getValue(), cb_gender.getValue(),
                        tf_f_name.getText(), tf_phone.getText(), doa,
                        cb_batch.getValue(), cb_course.getValue(), tf_address.getText(),
                        0, 0, 0, doa, picPath);

                if (MyDatabase.updateStudentInfoData(studentModal)) {
                    refreshTable(studentModal);
                    MyUtils.showSnackbar(main_pane, "Record updated successfully", 2);
                }

            }
        }

    }

    private void refreshTable(StudentModal sm) {
        StudentModal studentModal = table_view.getSelectionModel().getSelectedItem();
        studentModal.setName(sm.getName());
        studentModal.setS_class(sm.getS_class());
        studentModal.setGender(sm.getGender());
        studentModal.setBatch(sm.getBatch());
        studentModal.setCourse(sm.getCourse());
        studentModal.setDoa(sm.getDoa());
        studentModal.setPic(sm.getPic());
        table_view.refresh();

    }

    private void delete() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        ObservableList<StudentModal> selectedItems = table_view.getSelectionModel().getSelectedItems();

        MyAlert.confirmAlert(getClass(), selectedItems.size()==1?"Are u sure you wanna delete " +
                table_view.getSelectionModel().getSelectedItem().getName() + " record?" :
                "Are you sure you want to delete all selected record", () -> {

            for(int i=0; i<selectedItems.size(); i++)
                MyDatabase.delete("Student", selectedItems.get(i).getId());

            MyDatabase.masterDataObservableList.removeAll(selectedItems);

            MyUtils.showSnackbar(main_pane, "Record deleted successfully", 3);

        });

    }

}