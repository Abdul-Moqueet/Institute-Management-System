package setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import my_utils.MyAnimations;

import java.net.URL;
import java.util.ResourceBundle;

public class Setting implements Initializable {


    @FXML
    private Pane pane_current_password, pane_new_password, pane_confrim_password, pane;

    @FXML
    private JFXTextField tf_current_password, tf_new_password, tf_confirm_password;

    @FXML
    private Label lb_current_password_error, lb_new_password_error, lb_confirm_password_error;

    @FXML
    private JFXButton btn_save_chnages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setTranslateX(1066);

        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(pane, 0.5, 1066, 0, null));
    }


}
