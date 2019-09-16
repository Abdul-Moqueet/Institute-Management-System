package com.abdul_moqueet.my_utils;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {

    public static boolean isNotEmpty(TextField tf, Label lb, Pane pane){

        if(tf.getText().trim().isEmpty()){
            lb.setText("can't be empty");
            lb.setStyle("-fx-opacity: 1");
            MyAnimations.shake(pane, 0.08, 0, 10, 6, null);
            tf.requestFocus();
            return false;
        }
        lb.setStyle("-fx-opacity: 0");
        return true;

    }

    public static boolean isNotEmpty(String s, Label lb, Pane pane) {

        if (s.matches("")) {
            lb.setText("can't be empty");
            lb.setStyle("-fx-opacity: 1");
            MyAnimations.shake(pane, 0.08, 0, 10, 6, null);
            return false;
        }
        lb.setStyle("-fx-opacity: 0");
        return true;

    }

    public static boolean checkPhoneNo(String s, Label lb, Pane pane) {

        Pattern p = Pattern.compile("(0|91)?[0-9]{10}");
        Matcher m = p.matcher(s);

        if(!(m.find() && m.group().equals(s))){
            lb.setText("Invalid Phone No.");
            lb.setStyle("-fx-opacity: 1");
            MyAnimations.shake(pane, 0.08, 0, 10, 6, null);
            return false;
        }
        lb.setStyle("-fx-opacity: 0");
        lb.setText("can't be empty");
        return true;

    }

    public static void showSnackbar(Pane pane, String msg, int sec) {

        JFXSnackbar snackbar = new JFXSnackbar(pane);

        Label label = new Label(msg);
        label.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 30;");
        label.setPrefWidth(400);
        label.setPrefHeight(50);
        label.setFont(new Font(14));
        label.setAlignment(Pos.CENTER);

        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(label, Duration.seconds(sec)));
    }

    public static void setNumberOnly(TextField textField){

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
        });

    }

    public static void dateConverter(JFXDatePicker dp) {
        dp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
}
