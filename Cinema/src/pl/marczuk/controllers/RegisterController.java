package pl.marczuk.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.marczuk.service.ServerFacade;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController  implements Initializable {
    ServerFacade server;

    @FXML
    public Label suceedRegisterLabel;
    @FXML
    public TextField emailInputField;

    @FXML
    public PasswordField passwordInputField;

    @FXML
    public Button registerButton;

    @FXML
    public TextField lastNameInputField;

    @FXML
    public TextField firstNameInputField;

    @FXML
    public PasswordField confirmPasswordInputField;

    @FXML
    public Label emailErrorLabel;

    @FXML
    public Label firstNameErrorLabel;

    @FXML
    public Label lastNameErrorLabel;

    @FXML
    public Label passwordErrorLabel;

    @FXML
    public Label confirmPasswordErrorLabel;

    @FXML
    private Label Close;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane registerAnchorPane;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailInputField.getStylesheets().add("pl/marczuk/style/style.css");
        suceedRegisterLabel.setVisible(false);

    }

    @FXML
    private void handleClose(Event event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loadLogin(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pl/marczuk/fxml/Login.fxml"));
        Scene scene = loginButton.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(-scene.getHeight());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(registerAnchorPane);
        });
        timeline.play();
    }
    private boolean validateForm() {

        /////////  EMAIL /////////
        String emailRegexPattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        if(emailInputField.getText().length() == 0) {
            displayMessage(emailErrorLabel, "Pole adres email nie może być puste!");
            return false;
        }else {
            if (!emailInputField.getText().matches(emailRegexPattern)) {
                displayMessage(emailErrorLabel, "Podano zły format adresu email!");
                return false;
            }
            boolean userExists = false;
            try {
                userExists = server.checkMail(emailInputField.getText());
            } catch (RemoteException remoteException) {
                System.out.println("Cannot connect to RMI server");
            }
            if (userExists) {
                displayMessage(emailErrorLabel, "Podany email już istnieje!");
            }
        }
        //////////////////

        ///////// FIRST NAME /////////
        if(firstNameInputField.getText().length() == 0)  {
            displayMessage(firstNameErrorLabel, "Pole Imię nie może być puste!");
            return false;
        }
        //////////////////

        ///////// LAST NAME /////////
        if(lastNameInputField.getText().length() == 0)  {
            displayMessage(lastNameErrorLabel, "Pole Nazwisko nie może być puste!");
            return false;
        }
        //////////////////

        ///////// PASSWORD /////////
        if(passwordInputField.getText().length() == 0) {
            displayMessage(passwordErrorLabel, "Pole hasło nie może być puste!");
            return false;
        } else if(passwordInputField.getText().length() < 6) {
            displayMessage(passwordErrorLabel, "Pole hasło musi zawierać przynajmniej 6 znaków!");
            return false;
        }
        //////////////////

        ///////// CONFIRM PASSWORD /////////
        if(!confirmPasswordInputField.getText().equals(passwordInputField.getText())) {
            System.out.println();
            displayMessage(confirmPasswordErrorLabel, "Hasła różnią się!");
            return false;
        }

            return true;
    }
    private void displayMessage(Label errorLabel, String message) {
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


    public void signUp(Event event) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        }catch (RemoteException | NotBoundException remoteException) {
            displayMessage(confirmPasswordErrorLabel, "Błąd połączenia z serwerem!");
            return;
        }
        if(validateForm()){
            try {
                if(server.signUp(emailInputField.getText(), passwordInputField.getText(), firstNameInputField.getText(), lastNameInputField.getText())) {
                    suceedRegisterLabel.setVisible(true);
                    Thread.sleep(2000);
                    loadLogin(event);
                }
            }catch(RemoteException | SQLException remoteException) {
                displayMessage(confirmPasswordErrorLabel, "Błąd serwera, spróbuj ponownie później.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
