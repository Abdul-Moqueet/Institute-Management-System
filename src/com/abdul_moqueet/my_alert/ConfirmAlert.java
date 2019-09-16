package com.abdul_moqueet.my_alert;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmAlert implements Initializable {

    public Button btn_yes, btn_no;
    public AnchorPane anchorPane;
    public Label text;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_no.setOnAction(e->{
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        });
    }
}
