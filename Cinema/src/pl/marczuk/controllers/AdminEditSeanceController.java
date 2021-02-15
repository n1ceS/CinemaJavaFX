package pl.marczuk.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.Movie;
import pl.marczuk.model.Seance;
import pl.marczuk.model.fx.SeanceManagerFX;
import pl.marczuk.service.ServerFacade;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class AdminEditSeanceController implements Initializable {
    ServerFacade server;
    List<Movie> movieList;
    SeanceManagerFX seanceManagerFX;
    private SeancesManagerController seancesManagerController;
    ObservableList<String> movies = FXCollections.observableArrayList();
    @FXML
    public AnchorPane editSeanceAnchorPane;
    @FXML
    public JFXComboBox movieComboBox;
    @FXML
    public Label movieErrorLabel;
    @FXML
    public Label dateErrorLabel;
    @FXML
    public JFXDatePicker datePicker;
    @FXML
    public Label hourErrorLabel;
    @FXML
    public JFXTimePicker hourTimePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        }catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
    }
    public void setSeanceManagerController(SeancesManagerController seancesManagerController, SeanceManagerFX seanceManagerFX) throws RemoteException {
        this.seancesManagerController = seancesManagerController;
        this.seanceManagerFX = seanceManagerFX;
        //DUPLICATE
        movieList = server.getAllMovies().getList();
        for(Movie movie : movieList) {
            movies.add(movie.getTitle());
        }
        movieComboBox.setItems(movies);
        movieComboBox.getSelectionModel().select(seanceManagerFX.getTitleMovie());
        datePicker.setValue(seanceManagerFX.getDateSeance());
        //domyślnie w Scene Builderze nie działa
        hourTimePicker.set24HourView(true);
        hourTimePicker.setValue(seanceManagerFX.getHourSeance());
    }

    public void editSeance(MouseEvent mouseEvent) throws IOException, SQLException {
        if(validateForm()) {
            String selectedMovieName = movieComboBox.getSelectionModel().getSelectedItem().toString();
            Movie movie = movieList.stream().filter(movie1 -> movie1.getTitle().equals(selectedMovieName)).findFirst().get();
            LocalDate dateTime = datePicker.getValue();
            LocalTime hourTime = hourTimePicker.getValue();
            Seance seance = new Seance(movie, hourTime, dateTime);
            seance.setId(seanceManagerFX.getId());
            if(server.editSeance(seance)) {
                backToSeances(null);
                seancesManagerController.loadSeancesToTable(null);
            }
        }

    }
    public boolean validateForm() {
        if(movieComboBox.getSelectionModel().isEmpty()){
            displayMessage(movieErrorLabel, "Wybierz film!");
            return false;
        }
        if(datePicker.getValue() == null) {
            displayMessage(dateErrorLabel, "Wybierz datę seansu!");
            return false;
        }
        if(hourTimePicker.getValue() == null) {
            displayMessage(hourErrorLabel, "Wybierz godzinę seansu!");
            return false;
        }
        System.out.println(hourTimePicker.getValue().toString());
        return  true;
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
    public void backToSeances(MouseEvent mouseEvent) {
        Scene scene = dateErrorLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) editSeanceAnchorPane.getParent();

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(editSeanceAnchorPane.translateYProperty(), scene.getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(editSeanceAnchorPane);
        });
        timeline.play();
    }

}
