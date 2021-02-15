/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marczuk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.marczuk.service.ImageConverter;

import java.io.IOException;

/**
 *
 * @author Michal
 */
public class Cinema_Login extends Application {
    private static Stage pStage;

    public static Stage getpStage() {
        return pStage;
    }

    public static void setpStage(Stage pStage) {
        Cinema_Login.pStage = pStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.pStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }
    
}
