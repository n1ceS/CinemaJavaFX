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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.Movie;
import pl.marczuk.model.Seance;
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

public class AdminAddSeanceController implements Initializable {
    List<Movie> movieList;
    ServerFacade server;
    ObservableList<String> movies = FXCollections.observableArrayList();

    private SeancesManagerController seanceManagerController;
    @FXML
    public AnchorPane addSeanceAnchorPane;
    @FXML
    public ImageView movieImage;
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
    public void addSeance(MouseEvent mouseEvent) throws IOException, SQLException {
        if(validateForm()) {
            int movieId = Integer.parseInt(movieComboBox.getSelectionModel().getSelectedItem().toString().split(" ")[0]);
            Movie movie = movieList.stream().filter(movie1 -> movie1.getId() == movieId).findFirst().get();
            LocalDate dateTime = datePicker.getValue();
            LocalTime hourTime = hourTimePicker.getValue();
            Seance seance = new Seance(movie, hourTime, dateTime);
            if(server.addSeance(seance)) {
                backToSeances(null);
                seanceManagerController.loadSeancesToTable(null);
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

    public void setSeanceManagerController(SeancesManagerController seanceManagerController) throws RemoteException {
        this.seanceManagerController = seanceManagerController;
        movieList = server.getAllMovies().getList();
        for(Movie movie : movieList) {
            movies.add(movie.getId() +" " + movie.getTitle() + "(" +movie.getLength()+"min.)");
        }
        movieComboBox.setItems(movies);
        hourTimePicker.set24HourView(true);
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

        StackPane dashBoardStackPane = (StackPane) addSeanceAnchorPane.getParent();

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(addSeanceAnchorPane.translateYProperty(), scene.getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(addSeanceAnchorPane);
        });
        timeline.play();
    }
}
