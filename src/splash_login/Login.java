package splash_login;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import my_alert.ConfirmAlert;
import my_alert.MyAlert;
import my_utils.MyAnimations;
import my_utils.MyDatabase;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private AnchorPane anchor_login;

    @FXML
    private Label lb_date, lb_day, lb_time, lb_am_pm, invalid_user, invalid_pass;

    @FXML
    private ImageView btn_close;

    @FXML
    private Button btn_sign_in;

    @FXML
    private TextField tf_user, tf_pass;

    @FXML
    private Pane pane_user, pane_pass;

    private ConfirmAlert controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyDatabase.connectToDataBase();

        MyAnimations.fade(anchor_login, 3, 0, 1, null);
        clock();

        btn_close.setOnMouseEntered(e -> MyAnimations.rotate(btn_close, 0.3, 0, 360, 1));
        btn_close.setOnMouseExited(e -> MyAnimations.rotate(btn_close, 0.3, 360, 0, 1));
        btn_close.setOnMouseClicked(e ->

                controller = MyAlert.confirmAlert(getClass(), "Are you sure you want to exit?", () -> {
                    MyAnimations.fade(controller.anchorPane, 1, 1, 0, null);
                    MyAnimations.fade(anchor_login, 2, 1, 0, () -> System.exit(0));
                })
        );

        tf_user.setOnAction(e -> tf_pass.requestFocus());
        tf_pass.setOnAction(this::signIn);
        btn_sign_in.setOnAction(this::signIn);

    }

    private void clock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();

            String format = now.format(DateTimeFormatter.ofPattern("dd MMM eeee"));
            String day = format.substring(0, 2);
            String month = format.substring(3, 6);
            String dayName = format.substring(7);
            String time = now.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
            String am_pm = now.format(DateTimeFormatter.ofPattern("a"));

            lb_date.setText(day + " " + month);
            lb_day.setText(dayName);
            lb_time.setText(time);
            lb_am_pm.setText(am_pm);

        }), new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void signIn(ActionEvent e) {

//        if(MyUtils.isNotEmpty(tf_user, invalid_user, pane_user) && MyUtils.isNotEmpty(tf_pass, invalid_pass, pane_pass)){
//
//            if(!(tf_user.getText().equalsIgnoreCase("moqueet"))){
//                invalid_user.setText("Invalid username");
//                invalid_user.setStyle("-fx-opacity: 1");
//                MyAnimations.shake(pane_user, 0.08, 10, 6);
//                return;
//            }
//
//            invalid_user.setStyle("-fx-opacity: 0");
//
//            if(!(tf_pass.getText().equals("moqueet777"))){
//                invalid_pass.setText("Invalid password");
//                invalid_pass.setStyle("-fx-opacity: 1");
//                MyAnimations.shake(pane_pass, 0.08, 10, 6);
//                return;
//            }
//
//            invalid_pass.setStyle("-fx-opacity: 0");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/navigation_stage/navigation_stage.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            Node Node = (Node) e.getSource();
            Stage currentStage = (Stage) Node.getScene().getWindow();
            currentStage.close();

        } catch (Exception ex) {
            MyAlert.errorAlert(ex);
            ex.printStackTrace();
        }

    }
//    }
}
