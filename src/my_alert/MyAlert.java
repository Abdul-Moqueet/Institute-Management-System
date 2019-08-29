package my_alert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import my_utils.CallBack;

public class MyAlert {

    public static void errorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void okayAlert(Class context, String text) {

        OkayAlert controller = alertStage(context, "/my_alert/ok.fxml").getController();
        controller.text.setText(text);
        controller.anchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                Node node = (Node) e.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
            }
        });

    }

    public static ConfirmAlert confirmAlert(Class context, String text, CallBack callBack) {

        ConfirmAlert controller = alertStage(context, "/my_alert/confirm.fxml").getController();

        controller.anchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {

                if (e.getCode() == KeyCode.ENTER)
                    callBack.execute();

                Node node = (Node) e.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
            }
        });

        controller.text.setText(text);
        controller.btn_yes.setOnAction(e -> {
            callBack.execute();
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        });
        return controller;
    }

    private static FXMLLoader alertStage(Class context, String path) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(context.getResource(path));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            return fxmlLoader;

        } catch (Exception ex) {
            MyAlert.errorAlert(ex.toString());
        }
        return null;
    }

}
