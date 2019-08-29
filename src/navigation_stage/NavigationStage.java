package navigation_stage;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import my_alert.MyAlert;
import my_utils.CallBack;
import my_utils.MyAnimations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationStage implements Initializable {

    @FXML
    private BorderPane border_pane;

    @FXML
    private Pane nav_pane, hover_dash_board, hover_take_admission, hover_student_info, hover_payment,
            hover_signOut, hover_settings;

    @FXML
    private ImageView btn_back, btn_nav_menu, nav_bg_img;

    @FXML
    private Label nav_btn_dash_board, nav_btn_take_admission, nav_btn_student_info, nav_btn_payment,
            nav_btn_signOut, nav_btn_settings;

    private Timeline timeline;
    private int selected = 0, i = 0;
    private boolean isAnimating=true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nav_pane.setTranslateX(-300);
        activeNav();
        navBgImgAnimation();

        MyAnimations.delayUiThread(1, new CallBack() {
            @Override
            public void execute() {
                MyAnimations.move(nav_pane, 0.3, -300, 0, ()->
                        MyAnimations.shake(nav_pane, 0.17, -10, 0, 5, ()->animateNavBar()));
            }
        });

        btn_back.setOnMouseClicked(e-> MyAnimations.move(nav_pane, 0.3, 0, -300, null));

        btn_nav_menu.setOnMouseClicked(e-> MyAnimations.move(nav_pane, 0.3, -300, 0, ()->
                MyAnimations.shake(nav_pane, 0.17, -10, 0, 5, this::animateNavBar)));

        setOnMouseEnteredExited(nav_btn_dash_board, hover_dash_board);
        setOnMouseEnteredExited(nav_btn_take_admission, hover_take_admission);
        setOnMouseEnteredExited(nav_btn_student_info, hover_student_info);
        setOnMouseEnteredExited(nav_btn_payment, hover_payment);
        setOnMouseEnteredExited(nav_btn_signOut, hover_signOut);
        setOnMouseEnteredExited(nav_btn_settings, hover_settings);

//        setChildInBorderPane(0,"/dash_board/dash_board.fxml");

//        nav_btn_dash_board.setOnMouseClicked(e->
//                setChildInBorderPane(0,"/dash_board/dash_board.fxml"));
//
//        nav_btn_take_admission.setOnMouseClicked(e->
//            setChildInBorderPane(1,"/take_admission/take_admission.fxml"));
//
//        nav_btn_student_info.setOnMouseClicked(e ->
//                setChildInBorderPane(2, "/student_info/student_info.fxml"));
//
//        nav_btn_payment.setOnMouseClicked(e->
//                setChildInBorderPane(3, "/payment/payment.fxml"));
    }

    private void setChildInBorderPane(int i, String fxml){

        selected = i;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        border_pane.getChildren().remove(border_pane.getCenter());
        try {
            border_pane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            MyAlert.errorAlert(e.toString());
        }
        activeNav();

    }

    private void setOnMouseEnteredExited(Label label, Pane pane) {
        label.setOnMouseEntered(e->{
            if(!isAnimating)
                MyAnimations.expand(pane,0.5,300,1,null);
        });

        label.setOnMouseExited(e->{
            if(!isAnimating)
                MyAnimations.expand(pane, 0.5, 0, 1, null);
        });
    }

    private void animateNavBar() {
        isAnimating=true;
        MyAnimations.expand(hover_dash_board, 0.7, 300, 2, null);
        MyAnimations.expand(hover_take_admission, 0.8, 300, 2, null);
        MyAnimations.expand(hover_student_info, 0.9, 300, 2, null);
        MyAnimations.expand(hover_payment, 1, 300, 2, () -> isAnimating = false);
        MyAnimations.expand(hover_settings, 1.1, 300, 2, null);
        MyAnimations.expand(hover_signOut, 1.2, 300, 2, null);

    }

    private void activeNav(){

        hover_dash_board.setStyle("-fx-background-color: #8B22CC");
        hover_take_admission.setStyle("-fx-background-color: #8B22CC");
        hover_student_info.setStyle("-fx-background-color: #8B22CC");
        hover_payment.setStyle("-fx-background-color: #8B22CC");
        hover_signOut.setStyle("-fx-background-color: #8B22CC");
        hover_settings.setStyle("-fx-background-color: #8B22CC");


        switch (selected){

            case 0:
                hover_dash_board.setStyle("-fx-background-color: #1dcc49");
                break;

            case 1:
                hover_take_admission.setStyle("-fx-background-color: #1dcc49");
                break;

            case 2:
                hover_student_info.setStyle("-fx-background-color: #1dcc49");
                break;

            case 3:
                hover_payment.setStyle("-fx-background-color: #1dcc49");
                break;

            case 4:
                hover_signOut.setStyle("-fx-background-color: #1dcc49");
                break;

            case 5:
                hover_settings.setStyle("-fx-background-color: #1dcc49");
                break;
        }

    }

    private void navBgImgAnimation() {

        String[] bg = new String[6];

        for (int i = 0; i < 6; i++)
            bg[i] = "nav_bg_" + i + ".jpg";

        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            timeline.play();

            if (i >= 6) i = 0;

            MyAnimations.fade(nav_bg_img, 1, 1, 0, () -> {
                nav_bg_img.setImage(new Image(getClass().getResourceAsStream(bg[i++])));
                MyAnimations.fade(nav_bg_img, 1, 0, 1, () -> timeline.play());
            });

        }), new KeyFrame(Duration.seconds(5)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
