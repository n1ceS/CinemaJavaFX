package pl.marczuk.controllers;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.ListTransporter;
import pl.marczuk.model.Seance;
import pl.marczuk.model.fx.SeanceFX;
import pl.marczuk.service.ImageConverter;
import pl.marczuk.service.ServerFacade;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MoviesController implements Initializable {
    public static String IMAGE_SRC = "src\\pl\\marczuk\\images\\dashboard\\movies\\";

    private ServerFacade server;

    private ObservableList<SeanceFX> seances = FXCollections.observableArrayList();
    @FXML
    public Label emptyTableLabel;
    @FXML
    public ImageView emptyTableImage;
    @FXML
    public AnchorPane moviesAnchorPane;
    @FXML
    public TableView<SeanceFX> movieTable;
    @FXML
    public TableColumn<SeanceFX, ImageView> imageMovie;
    @FXML
    public TableColumn<Seance, String> categoryMovie;
    @FXML
    public TableColumn<Seance, String> hourMovie;
    @FXML
    public TableColumn<Seance, String> freeSeatsMovie;
    @FXML
    public JFXDatePicker datePicker;
    @FXML
    public TableColumn<Seance, Integer> movieId;
    @FXML
    public TableColumn titleMovie;

    public void loadMoviesToTable(ActionEvent event) throws IOException {
        seances.removeAll();
        movieTable.getItems().clear();
        ListTransporter seancesFromServer = server.getByDate(datePicker.getValue());
        List<Seance> seanceList = seancesFromServer.getList();
        if(seanceList.size() == 0 ) {
            emptyTableImage.setVisible(true);
            emptyTableLabel.setVisible(true);
        }else {
            emptyTableImage.setVisible(false);
            emptyTableLabel.setVisible(false);
        }
        for (Seance seance : seanceList) {
            ImageConverter.byteToJpg(seance.getMovie().getImage(), "\\movies\\" + seance.getMovie().getId() + ".jpg");
            File file = new File(IMAGE_SRC + seance.getMovie().getId() + ".jpg");
            Image image = new Image(file.toURI().toString(), 150, 200, false, false);
            ImageView imageView = new ImageView(image);
            SeanceFX seanceFX = new SeanceFX(seance.getId(), seance.getMovie().getTitle(),seance.getMovie().getId(), seance.getMovie().getCategory(), seance.getDate(),seance.getStartTime(), server.getFreeSeats(seance.getId()).toString(), imageView);
            seances.add(seanceFX);
        }
        movieTable.setItems(seances);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        } catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
        imageMovie.setCellValueFactory(new PropertyValueFactory<>("movieImage"));
        categoryMovie.setCellValueFactory(new PropertyValueFactory<>("category"));
        hourMovie.setCellValueFactory(new PropertyValueFactory<>("date"));
        freeSeatsMovie.setCellValueFactory(new PropertyValueFactory<>("freeSeats"));
        movieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleMovie.setCellValueFactory(new PropertyValueFactory<>("movieName"));

        //USTAWIAM BRAK WIADOMOSCI W PRZYPADKU PUSTEJ TABELI
        movieTable.setPlaceholder(new Label(""));
    }

    public void getDetails(MouseEvent event) throws IOException, SQLException {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            SeanceFX seanceFX = movieTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/MovieDetails.fxml"));
            Parent root = loader.load();
            MovieDetailsController movieDetailsController = loader.getController();
            movieDetailsController.setDetails(seanceFX.getMovieId(), seanceFX.getSeanceDate(),seanceFX.getSeanceStartTime(), seanceFX.getFreeSeats(), seanceFX.getId());
            loadMovieDetailsWindow(root);
        }
    }

    private void loadMovieDetailsWindow(Parent root) throws IOException {
        Scene scene = datePicker.getScene();

        StackPane dashBoardStackPane = (StackPane) moviesAnchorPane.getParent();
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


