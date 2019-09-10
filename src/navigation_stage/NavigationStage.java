package navigation_stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import my_alert.MyAlert;
import my_utils.CallBack;
import my_utils.MyAnimations;

import java.awt.Desktop;
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
    private ImageView  btn_nav_menu, img_stack, img_github, img_linked_in, img_portfolio;

    @FXML
    private Label nav_btn_dash_board, nav_btn_take_admission, nav_btn_student_info, nav_btn_payment,
            nav_btn_signOut, nav_btn_setting;

    private int selected = 0;
    private boolean isAnimating=true, isOpen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nav_pane.setTranslateX(-300);
        activeNav();

        MyAnimations.delayUiThread(2, new CallBack() {
            @Override
            public void execute() {
                MyAnimations.move(nav_pane, 0.3, -300, 0, ()->
                        MyAnimations.shake(nav_pane, 0.17, -10, 0, 5, ()->animateNavBar()));
            }
        });

        btn_nav_menu.setOnMouseClicked(e-> {

            if(isOpen){
                isOpen=false;
                 MyAnimations.move(nav_pane, 0.3, -300, 0, () ->
                    MyAnimations.shake(nav_pane, 0.17, -10, 0, 5, this::animateNavBar));
            }else {
                MyAnimations.move(nav_pane, 0.3, 0, -300, null);
                isOpen = true;
            }

        });

        setOnMouseEnteredExited(nav_btn_dash_board, hover_dash_board);
        setOnMouseEnteredExited(nav_btn_take_admission, hover_take_admission);
        setOnMouseEnteredExited(nav_btn_student_info, hover_student_info);
        setOnMouseEnteredExited(nav_btn_payment, hover_payment);
        setOnMouseEnteredExited(nav_btn_signOut, hover_signOut);
        setOnMouseEnteredExited(nav_btn_setting, hover_settings);

        setChildInBorderPane(0,"/dash_board/dash_board.fxml");

        nav_btn_dash_board.setOnMouseClicked(e->
                setChildInBorderPane(0,"/dash_board/dash_board.fxml"));

        nav_btn_take_admission.setOnMouseClicked(e->
            setChildInBorderPane(1,"/take_admission/take_admission.fxml"));

        nav_btn_student_info.setOnMouseClicked(e ->
                setChildInBorderPane(2, "/student_info/student_info.fxml"));

        nav_btn_payment.setOnMouseClicked(e->
                setChildInBorderPane(3, "/payment_info/payment_info.fxml"));

        nav_btn_setting.setOnMouseClicked(e->
                setChildInBorderPane(4, "/setting/setting.fxml"));

        nav_btn_signOut.setOnMouseClicked(e->
                MyAlert.confirmAlert(getClass(), "Are you sure u wanna signOut?",()->{

                    Node node = (Node) e.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();

                    try {

                        Parent root = FXMLLoader.load(getClass().getResource("/splash_login/login.fxml"));
                        Scene scene = new Scene(root);
                        scene.setFill(Color.TRANSPARENT);
                        Stage loginStage = new Stage();
                        loginStage.setScene(scene);
                        loginStage.initStyle(StageStyle.TRANSPARENT);
                        loginStage.show();

                    } catch (Exception ex) {
                        MyAlert.errorAlert(ex);
                    }

                }));

        setSocialIconHover(img_github, "github");
        setSocialIconHover(img_linked_in, "linked_in");
        setSocialIconHover(img_stack, "stack");
        setSocialIconHover(img_portfolio, "web");

        img_github.setOnMouseClicked(e->launchUrl("https://github.com/Abdul-Moqueet"));
        img_linked_in.setOnMouseClicked(e->launchUrl("https://www.linkedin.com/in/abdul-moqueet"));
        img_stack.setOnMouseClicked(e->launchUrl("https://stackoverflow.com/users/6912209/abdul-moqueet"));
        img_portfolio.setOnMouseClicked(e->launchUrl("https://abdul-moqueet.github.io"));

    }


    private void launchUrl(String url){

        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setChildInBorderPane(int i, String fxml){

        selected = i;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        border_pane.getChildren().remove(border_pane.getCenter());
        try {
            border_pane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            MyAlert.errorAlert(e);
        }
        activeNav();

    }

    private void setSocialIconHover(ImageView imageView, String name){

        Image hoverImg = new Image(getClass().getResourceAsStream(name+"_f.png"));
        Image unHoverImg = new Image(getClass().getResourceAsStream(name+".png"));

        imageView.setOnMouseEntered(e->imageView.setImage(hoverImg));
        imageView.setOnMouseExited(e->imageView.setImage(unHoverImg));

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
                hover_dash_board.setStyle("-fx-background-color: #1dcc63");
                break;

            case 1:
                hover_take_admission.setStyle("-fx-background-color: #1DCC63");
                break;

            case 2:
                hover_student_info.setStyle("-fx-background-color: #1DCC63");
                break;

            case 3:
                hover_payment.setStyle("-fx-background-color: #1DCC63");
                break;

            case 4:
                hover_settings.setStyle("-fx-background-color: #1DCC63");
                break;

            case 5:
                hover_signOut.setStyle("-fx-background-color: #1DCC63");
                break;
        }
    }
}