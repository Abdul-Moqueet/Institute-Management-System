package take_admission;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import my_alert.MyAlert;
import my_utils.MyAnimations;
import my_utils.MyDatabase;
import my_utils.MyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static my_utils.MyUtils.*;

public class TakeAdmission implements Initializable {

    @FXML
    private Pane take_admission_pane, pane_name, pane_phone_no, pane_f_name, pane_address, mainPane;

    @FXML
    private ImageView btn_save, btn_clear_all, btn_refresh, img_v_pic;

    @FXML
    private Label lb_name_error, lb_f_name_error, lb_phone_no_error, lb_address_error;

    @FXML
    private TextField tf_name, tf_phone_no, tf_f_name, tf_address, tf_date_of_admission, tf_total_fee, tf_dues, tf_pay;

    @FXML
    private JFXComboBox<String> cb_class, cb_gender, cb_course, cb_batch;

    @FXML
    private Button btn_browse;

    private String picName = "default_avatar.png";

    private int[] rate = {2000, 2500, 3000, 5000, 3500};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        take_admission_pane.setTranslateX(1066);

        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(take_admission_pane, 0.5, 1066, 0, null));

        Tooltip.install(btn_save, new Tooltip("Save"));
        Tooltip.install(btn_clear_all, new Tooltip("Clear All"));
        Tooltip.install(btn_refresh, new Tooltip("Refresh"));

        setHover(btn_save, "save");
        setHover(btn_clear_all, "clear_all");
        setHover(btn_refresh, "refresh");

        cb_class.getItems().addAll("BCA-I", "BCA-II", "BCA-III", "MCA-I", "MCA-II", "MCA-III");
        cb_class.setValue(cb_class.getItems().get(0));

        cb_gender.getItems().addAll("Male", "Female");
        cb_gender.setValue(cb_gender.getItems().get(0));

        cb_course.getItems().addAll("C-Language", "C++", "Java", "JavaScript", "Python");
        cb_course.setValue(cb_course.getItems().get(0));

        cb_batch.getItems().addAll("08:00 AM", "11:00 AM", "04:00 PM");
        cb_batch.setValue(cb_batch.getItems().get(0));

        tf_total_fee.setText("2000");
        tf_dues.setText("2000");

        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        tf_date_of_admission.setText(now);

        btn_save.setOnMouseClicked(e -> save());
        tf_address.setOnAction(e -> save());
        btn_clear_all.setOnMouseClicked(e -> clearAll());
        btn_refresh.setOnMouseClicked(e -> refresh());

        btn_browse.setOnAction(e -> chooseFile(e, img_v_pic));

        setNumberOnly(tf_phone_no);
        setNumberOnly(tf_pay);

        cb_course.setOnAction(e-> calculate());

        tf_pay.textProperty().addListener((observable, oldValue, newValue) -> {

            int total = Integer.parseInt(tf_total_fee.getText());
            int pay=0;
            if(tf_pay.getText().isEmpty())
                tf_dues.setText(tf_total_fee.getText());
            else {
                pay = Integer.parseInt(newValue);
                tf_dues.setText(String.valueOf(total - pay));
            }
            if(pay>total)
                tf_pay.setText(tf_total_fee.getText());

        });

        tf_pay.setOnAction(e->save());

    }

    private void calculate() {

        int index = cb_course.getSelectionModel().getSelectedIndex();

        tf_total_fee.setText(String.valueOf(rate[index]));
        if(tf_pay.getText().trim().matches(""))
            tf_dues.setText(tf_total_fee.getText());
        else
            tf_dues.setText(String.valueOf(rate[index] - Integer.parseInt(tf_pay.getText())));

    }

    private void clearAll() {
        tf_name.clear();
        tf_phone_no.clear();
        tf_f_name.clear();
        tf_address.clear();

        MyAnimations.shake(pane_name, 0.08, 0, 10, 6, null);
        MyAnimations.shake(pane_phone_no, 0.08, 0, 10, 6, null);
        MyAnimations.shake(pane_f_name, 0.08, 0, 10, 6, null);
        MyAnimations.shake(pane_address, 0.08, 0, 10, 6, null);

    }

    private void refresh() {
        MyAnimations.shake(take_admission_pane, 0.08, 0, 10, 6, () -> {
            clearAll();
            picName = "default_avatar.png";
            img_v_pic.setImage(new Image(getClass().getResourceAsStream("default_avatar.png")));
        });
    }

    public String chooseFile(ActionEvent e, ImageView imageView) {

        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.jpe", "*.png");

        fileChooser.getExtensionFilters().add(fileExtensions);

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null)
            return "";

        picName = System.getProperty("user.dir") + "\\Institute_Management\\images\\" + selectedFile.getName();

        if (copyImage(selectedFile)) {
            try {
                imageView.setImage(new Image(new FileInputStream(picName)));
                return picName;
            } catch (FileNotFoundException ex) {
                MyAlert.errorAlert(ex);
            }

        }
        return "";
    }

    private boolean copyImage(File selectedFile) {

        try {
            if (selectedFile != null) {
                Path from = Paths.get(selectedFile.toURI());
                Path to = Paths.get(picName);
                try {
                    Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception ex){
                    Files.copy(from, to.resolve(from), StandardCopyOption.REPLACE_EXISTING);
                }

                return true;
            }
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return false;
    }

    private void setHover(ImageView imageView, String file) {
        imageView.setOnMouseEntered(e -> imageView.setImage(new Image(getClass().getResourceAsStream(file + "_f.png"))));
        imageView.setOnMouseExited(e -> imageView.setImage(new Image(getClass().getResourceAsStream(file + ".png"))));
    }

    private void save() {

        if (isNotEmpty(tf_name, lb_name_error, pane_name) && isNotEmpty(tf_f_name, lb_f_name_error, pane_f_name)
                && isNotEmpty(tf_phone_no, lb_phone_no_error, pane_phone_no) && isNotEmpty(tf_address, lb_address_error, pane_address)) {

            if(checkPhoneNo(tf_phone_no.getText(), lb_phone_no_error, pane_phone_no)){

                if (picName.trim().isEmpty())
                    picName = "default_avatar.png";

                StudentModal studentModal = new StudentModal(0, tf_name.getText(), cb_class.getValue(), cb_gender.getValue(),
                                tf_f_name.getText(), tf_phone_no.getText(), tf_date_of_admission.getText(),
                                cb_batch.getValue(), cb_course.getValue(), tf_address.getText(),
                                Integer.parseInt(tf_total_fee.getText()), Integer.parseInt(tf_dues.getText()),
                                Integer.parseInt(tf_pay.getText()), tf_date_of_admission.getText(), picName);

                if(MyDatabase.saveAdmission(studentModal)){
                    MyUtils.showSnackbar(mainPane, "Record Added Successfully", 3);
                }

            }

        }

    }

}
