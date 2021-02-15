package pl.marczuk.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.ListTransporter;
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

public class SeancesManagerController implements Initializable {

    private ObservableList<SeanceManagerFX> movies = FXCollections.observableArrayList();
    @FXML
    public AnchorPane seancesAnchorPane;
    @FXML
    public ImageView emptyTableImage;
    @FXML
    public Label emptyTableLabel;
    @FXML
    public TableView<SeanceManagerFX> seanceTable;
    @FXML
    public TableColumn<SeanceManagerFX, Integer> movieId;
    @FXML
    public TableColumn<SeanceManagerFX, String> titleMovie;
    @FXML
    public TableColumn<SeanceManagerFX, LocalDate> dateSeance;
    @FXML
    public TableColumn<SeanceManagerFX, LocalTime> hourSeance;
    @FXML
    public JFXButton addButton;
    @FXML
    public JFXButton deleteButton;
    @FXML
    public JFXButton editButton;


    ServerFacade server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        } catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
        movieId.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        titleMovie.setCellValueFactory(new PropertyValueFactory<>("titleMovie"));
        dateSeance.setCellValueFactory(new PropertyValueFactory<>("dateSeance"));
        hourSeance.setCellValueFactory(new PropertyValueFactory<>("hourSeance"));
    }
    public void loadSeancesToTable(ActionEvent event) throws IOException, SQLException {
        movies.removeAll();
        seanceTable.getItems().clear();
        ListTransporter seancesFromServer = server.getAllSeances();
        List<Seance> seanceList = seancesFromServer.getList();
        if(seanceList.size() == 0 ) {
            emptyTableImage.setVisible(true);
            emptyTableLabel.setVisible(true);
            return;
        }else {
            emptyTableImage.setVisible(false);
            emptyTableLabel.setVisible(false);
        }
        for ( Seance seance: seanceList) {

            SeanceManagerFX seanceManagerFX = new SeanceManagerFX(seance.getId(), seance.getMovie().getTitle(), seance.getMovie().getId(), seance.getDate(), seance.getStartTime());
            movies.add(seanceManagerFX);
        }
        seanceTable.setItems(movies);
    }

    public void addSeance(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/AdminAddSeance.fxml"));
        Parent root = loader.load();
        AdminAddSeanceController adminAddSeanceController = loader.getController();
        adminAddSeanceController.setSeanceManagerController(this);
        loadAddSeanceWindow(root);
    }

    public void deleteSeance(MouseEvent mouseEvent) throws IOException, SQLException {
        SeanceManagerFX seanceManagerFX = seanceTable.getSelectionModel().getSelectedItem();
        if(seanceManagerFX == null) return;
        if(server.deleteSeance(seanceManagerFX.getId())) {
            loadSeancesToTable(null);
        }
    }

    public void editSeance(MouseEvent mouseEvent) throws IOException {
        SeanceManagerFX seanceManagerFX = seanceTable.getSelectionModel().getSelectedItem();
        if(seanceManagerFX == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/AdminEditSeance.fxml"));
        Parent root = loader.load();
        AdminEditSeanceController adminEditSeanceController = loader.getController();
        adminEditSeanceController.setSeanceManagerController(this, seanceManagerFX);
        loadAddSeanceWindow(root);
    }
    private void loadAddSeanceWindow(Parent root) throws IOException {
        Scene scene = emptyTableLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) seancesAnchorPane.getParent();
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
