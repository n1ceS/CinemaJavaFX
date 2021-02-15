/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marczuk.controllers;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pl.marczuk.Cinema_Login;
import pl.marczuk.model.Session;
import pl.marczuk.service.ServerFacade;

/**
 *
 * @author Michal
 */
public class LoginController implements Initializable {
    ServerFacade server;
    // ZMIENNE DO "PRZESUWANIA" OKNEM
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    public PasswordField passwordInputField;

    @FXML
    public TextField emailInputField;

    @FXML
    public Label errorLabel;

    @FXML
    private Label Close;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private StackPane parentContainer;

    @FXML
    private void handleClose(Event event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        anchorRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        anchorRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cinema_Login.getpStage().setX(event.getScreenX() - xOffset);
                Cinema_Login.getpStage().setY(event.getScreenY() - yOffset);
            }
        });
    }
    @FXML
    private void loadRegister(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pl/marczuk/fxml/Register.fxml"));
        Scene scene = registerButton.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(anchorRoot);
        });
        timeline.play();
    }

    public void signIn(Event mouseEvent) throws NotBoundException {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        }catch (RemoteException | NotBoundException remoteException) {
            displayMessage("Błąd połączenia z serwerem!");
            return;
        }
        if (validateInput()) {
            String email =  emailInputField.getText();
            String password = passwordInputField.getText();
            try {
                Session session = server.signIn(email, password);
                if(session != null)
                 {
                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/Dashboard.fxml"));
                     Parent root = loader.load();

                     Stage stage = new Stage();
                     stage.setScene(new Scene(root));
                     stage.setTitle("Kino Sława");
                     //USTAWIAM TO JAKO GLOWNE OKNO
                     Cinema_Login.setpStage(stage);
                     //USTAWIAM SESJE KLIENTA
                     DashboardController dashboardController = loader.getController();
                     dashboardController.setCurrentSession(session);
                     stage.initStyle(StageStyle.UNDECORATED);
                     handleClose(null);
                     stage.show();
                     dashboardController.loadHome(null);
                }
                else
                {
                    displayMessage("Niepoprawne dane logowania!");
                }
            }
            catch (RemoteException | SQLException ex) {
                displayMessage("Nie można nawiązać połączenia");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean validateInput() {
        String emailRegexPattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        if(!emailInputField.getText().matches(emailRegexPattern) || emailInputField.getText().length() == 0 || passwordInputField.getText().length() == 0){
            displayMessage("Niepoprawne dane logowania!");
            return false;
        }
        return true;
    }

    private void displayMessage(String message) {
        errorLabel.setText(message);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    errorLabel.setVisible(true);
                    Thread.sleep(2000);
                    errorLabel.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

}
