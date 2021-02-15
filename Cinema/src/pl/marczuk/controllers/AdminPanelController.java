package pl.marczuk.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

public class AdminPanelController implements Initializable {
    ServerFacade server;

    @FXML
    public AnchorPane adminPanelAnchorPane;
    @FXML
    public JFXButton manageReservationsButton;
    @FXML
    public JFXButton manageSeancesButton;
    @FXML
    public JFXButton manageMoviesButton;
    @FXML
    public JFXButton manageUsersButton;
    @FXML
    public Label reservationCount;
    @FXML
    public Label moviesCount;
    @FXML
    public Label usersCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        } catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
        try {
            reservationCount.setText(String.valueOf(server.getReservationsCount()));
            usersCount.setText(String.valueOf(server.getUsersCount()));
            moviesCount.setText(String.valueOf(server.getMoviesCount()));
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    @FXML
    public void loadReservationsManager(MouseEvent mouseEvent) {
    }

    @FXML
    public void loadSeancesManager(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/SeancesManager.fxml"));
        Parent root = loader.load();
        SeancesManagerController seancesManagerController = loader.getController();
        seancesManagerController.loadSeancesToTable(null);
        loadMovieDetailsWindow(root);
    }

    @FXML
    public void loadMoviesManager(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/MoviesManager.fxml"));
        Parent root = loader.load();
        MoviesManagerController moviesManagerController = loader.getController();
        moviesManagerController.loadMoviesToTable(null);
        loadMovieDetailsWindow(root);
    }

    @FXML
    public void loadUsersManager(MouseEvent mouseEvent) {
    }

    private void loadMovieDetailsWindow(Parent root) throws IOException {
        Scene scene = manageMoviesButton.getScene();

        StackPane dashBoardStackPane = (StackPane) adminPanelAnchorPane.getParent();
        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        dashBoardStackPane.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
        });
        timeline.play();
    }

}
