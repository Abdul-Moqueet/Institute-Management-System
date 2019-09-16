package com.abdul_moqueet.dash_board;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;

import javafx.scene.text.Text;
import com.abdul_moqueet.my_utils.MyAnimations;
import com.abdul_moqueet.my_utils.MyDatabase;

import java.net.URL;

import java.util.HashMap;
import java.util.ResourceBundle;

public class DashBoard implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private PieChart pie_Chat;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Text t_boys, t_girls;

    private HashMap<String, Integer> chartDataHashMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setTranslateX(1066);

        chartDataHashMap = MyDatabase.getChartDataMap();

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Boys");

        XYChart.Series<String, Number> dataSeries2 = new XYChart.Series<>();
        dataSeries2.setName("Girls");

        dataSeries1.getData().add(new XYChart.Data<>("Total", 0));
        dataSeries1.getData().add(new XYChart.Data<>("Paid", 0));
        dataSeries1.getData().add(new XYChart.Data<>("Dues", 0));

        dataSeries2.getData().add(new XYChart.Data<>("Total", 0));
        dataSeries2.getData().add(new XYChart.Data<>("Paid", 0));
        dataSeries2.getData().add(new XYChart.Data<>("Dues", 0));

        barChart.getData().add(dataSeries1);
        barChart.getData().add(dataSeries2);

        MyAnimations.delayUiThread(0.2, () -> MyAnimations.move(pane, 0.5, 1066, 0, () -> {
            loadCharts(dataSeries1, dataSeries2);
        }));

    }

    private void loadCharts(XYChart.Series<String, Number> dataSeries1, XYChart.Series<String, Number> dataSeries2) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(

                new PieChart.Data("Boys", chartDataHashMap.get("boys")),
                new PieChart.Data("Girls", chartDataHashMap.get("girls")),
                new PieChart.Data("Paid", chartDataHashMap.get("paid")),
                new PieChart.Data("Dues", chartDataHashMap.get("dues")));

        pie_Chat.setData(pieChartData);
        t_boys.setText(chartDataHashMap.get("boys").toString());
        t_girls.setText(chartDataHashMap.get("girls").toString());

        dataSeries1.getData().add(new XYChart.Data<>("Total", chartDataHashMap.get("boys")));
        dataSeries1.getData().add(new XYChart.Data<>("Paid", chartDataHashMap.get("boysPaid")));
        dataSeries1.getData().add(new XYChart.Data<>("Dues", chartDataHashMap.get("boysDues")));

        dataSeries2.getData().add(new XYChart.Data<>("Total", chartDataHashMap.get("girls")));
        dataSeries2.getData().add(new XYChart.Data<>("Paid", chartDataHashMap.get("girlsPaid")));
        dataSeries2.getData().add(new XYChart.Data<>("Dues", chartDataHashMap.get("girlsDues")));

    }

}
