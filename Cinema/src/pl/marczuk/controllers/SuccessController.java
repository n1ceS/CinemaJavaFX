package pl.marczuk.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class SuccessController  {

    @FXML
    public AnchorPane successWindowAnchorPane;
    @FXML
    public Label successHeader;
    @FXML
    public Label successContent;

    public void setContent(String header, String body) {
        successHeader.setText(header);
        successContent.setText(body);
    }
    @FXML
    private void loadHome(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pl/marczuk/fxml/Home.fxml"));
        Scene scene = successHeader.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        StackPane parentContainer = (StackPane) successWindowAnchorPane.getParent();


        //USUWAM WSZYSTKIE DOTYCHCZASOWE OKNA
        parentContainer.getChildren().clear();
        // I DODAJE JEDNO TYLKO OKNO STRONY GLOWNEJ
        parentContainer.getChildren().add(root);
        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
        });
        timeline.play();
    }
}
