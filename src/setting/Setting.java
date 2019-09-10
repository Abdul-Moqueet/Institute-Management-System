package setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import my_utils.MyAnimations;
import my_utils.MyDatabase;
import my_utils.MyUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Setting implements Initializable {


    @FXML
    private Pane pane_current_password, pane_new_password, pane_confrim_password, pane;

    @FXML
    private JFXTextField tf_current_password, tf_new_password, tf_confirm_password;

    @FXML
    private Label lb_current_password_error, lb_new_password_error, lb_confirm_password_error;

    @FXML
    private JFXButton btn_save_change;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setTranslateX(1066);

        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(pane, 0.5, 1066, 0, null));

        btn_save_change.setOnAction(e->changePassword());


    }

    private void changePassword() {

        ArrayList<String> credentials = MyDatabase.getCredentials();

        if(MyUtils.isNotEmpty(tf_current_password, lb_current_password_error, pane_current_password) &&
                MyUtils.isNotEmpty(tf_confirm_password, lb_confirm_password_error, pane_confrim_password) &&
                MyUtils.isNotEmpty(tf_new_password, lb_new_password_error, pane_new_password)){

            if(!credentials.get(1).equals(tf_current_password.getText())){
                itsAnError(tf_current_password, lb_current_password_error, pane_current_password, "Password is wrong!");
                return;
            }
            lb_current_password_error.setStyle("-fx-opacity: 0");

            if(!tf_new_password.getText().equals(tf_confirm_password.getText())){
                itsAnError(tf_confirm_password, lb_confirm_password_error, pane_confrim_password, "Password doesn't match");
                itsAnError(tf_new_password, lb_new_password_error, pane_new_password, "Password doesn't match");
                return;
            }
            lb_new_password_error.setStyle("-fx-opacity: 0");
            lb_confirm_password_error.setStyle("-fx-opacity: 0");

            if(MyDatabase.updateCredentials(tf_new_password.getText())){

                tf_current_password.clear();
                tf_new_password.clear();
                tf_confirm_password.clear();

                MyUtils.showSnackbar(pane, "Password changed successfully", 4);

            }

        }

    }

    private static void itsAnError(TextField tf, Label lb, Pane pane, String errorMsg){

        if(tf.getText().trim().isEmpty()){
            lb.setText(errorMsg);
            lb.setStyle("-fx-opacity: 1");
            MyAnimations.shake(pane, 0.08, 0, 10, 6, null);
            tf.requestFocus();
        }
    }


}
