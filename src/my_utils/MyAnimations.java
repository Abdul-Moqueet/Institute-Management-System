package my_utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MyAnimations {

    public static void delayUiThread(double sec, CallBack callBack){

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(sec)));
        timeline.play();
        timeline.setOnFinished(e->callBack.execute());

    }

    public static void fade(Node node, double sec, double start, double end, CallBack callBack){
        FadeTransition ft = new FadeTransition(Duration.seconds(sec), node);
        ft.setFromValue(start);
        ft.setToValue(end);
        ft.play();
        if(callBack!=null)
            ft.setOnFinished(e->callBack.execute());
    }

    public static void rotate(Node node, double sec, double start, double end, int cycle){
        RotateTransition rt = new RotateTransition(Duration.seconds(sec), node);
        rt.setFromAngle(start);
        rt.setToAngle(end);
        rt.setCycleCount(cycle);
        rt.play();
    }


    public static void shake(Node node, double sec, double start, double end, int cycle, CallBack callBack){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(sec), node);
        tt.setFromX(start);
        tt.setToX(end);
        tt.setAutoReverse(true);
        tt.setCycleCount(cycle);
        tt.play();
        if(callBack!=null)
            tt.setOnFinished(e->callBack.execute());

    }

    public static void move(Node node, double sec, double start, double end, CallBack callBack){
        TranslateTransition tt = new TranslateTransition(Duration.seconds(sec), node);
        tt.setFromX(start);
        tt.setToX(end);
        tt.play();
        if(callBack!=null)
            tt.setOnFinished(e->callBack.execute());
    }

    public static void expand(Pane node, double sec, double end, int cycle, CallBack callBack){
        KeyValue widthValue = new KeyValue(node.minWidthProperty(), end);
        KeyFrame frame = new KeyFrame(Duration.seconds(sec), widthValue);
        Timeline timeline = new Timeline(frame);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(cycle);
        timeline.play();
        if(callBack!=null)
            timeline.setOnFinished(e->callBack.execute());
    }

}
