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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.ListTransporter;
import pl.marczuk.model.Movie;
import pl.marczuk.model.Seance;
import pl.marczuk.model.fx.MovieFX;
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

public class MoviesManagerController implements Initializable {
    public static String IMAGE_SRC = "src\\pl\\marczuk\\images\\dashboard\\movies\\";
    public JFXButton addButton;
    public JFXButton deleteButton;
    public JFXButton editButton;

    private ServerFacade server;

    private ObservableList<MovieFX> movies = FXCollections.observableArrayList();
    @FXML
    public Label emptyTableLabel;
    @FXML
    public ImageView emptyTableImage;
    @FXML
    public AnchorPane moviesManagerAnchorPane;
    @FXML
    public TableView<MovieFX> movieManagerTable;
    @FXML
    public TableColumn<MovieFX, ImageView> imageMovie;
    @FXML
    public TableColumn<MovieFX, String> categoryMovie;
    @FXML
    public TableColumn<MovieFX, String> lengthMovie;
    @FXML
    public TableColumn<MovieFX, String> descriptionMovie;
    @FXML
    public TableColumn<MovieFX, String> yearMovie;
    @FXML
    public TableColumn<Seance, Integer> movieId;
    @FXML
    public TableColumn titleMovie;

    public void loadMoviesToTable(ActionEvent event) throws IOException {
        movies.removeAll();
        movieManagerTable.getItems().clear();
        ListTransporter seancesFromServer = server.getAllMovies();
        List<Movie> movieList = seancesFromServer.getList();
        if(movieList.size() == 0 ) {
            emptyTableImage.setVisible(true);
            emptyTableLabel.setVisible(true);
        }else {
            emptyTableImage.setVisible(false);
            emptyTableLabel.setVisible(false);
        }
        for ( Movie movieItem: movieList) {
            ImageConverter.byteToJpg(movieItem.getImage(), "\\movies\\" + movieItem.getId() + ".jpg");
            File file = new File(IMAGE_SRC + movieItem.getId() + ".jpg");
            Image image = new Image(file.toURI().toString(), 150, 200, false, false);
            ImageView imageView = new ImageView(image);
            MovieFX movieFX = new MovieFX(movieItem.getId(), movieItem.getTitle(), movieItem.getCategory(), movieItem.getDescription(), String.valueOf(movieItem.getLength()),String.valueOf(movieItem.getYear()),  imageView);
            movies.add(movieFX);
        }
        movieManagerTable.setItems(movies);
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
        lengthMovie.setCellValueFactory(new PropertyValueFactory<>("length"));
        yearMovie.setCellValueFactory(new PropertyValueFactory<>("year"));
        descriptionMovie.setCellValueFactory(new PropertyValueFactory<>("description"));
        movieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleMovie.setCellValueFactory(new PropertyValueFactory<>("movieName"));

        //USTAWIAM BRAK WIADOMOSCI W PRZYPADKU PUSTEJ TABELI
        movieManagerTable.setPlaceholder(new Label(""));
    }

    public void addMovieButton(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/AdminAddMovie.fxml"));
        Parent root = loader.load();
        AdminAddMovieController adminAddMovieController = loader.getController();
        adminAddMovieController.setMovieManagerController(this);
        loadAddMovieWindow(root);
    }

    public void editMovie(MouseEvent mouseEvent) throws IOException {
        MovieFX movieFX = movieManagerTable.getSelectionModel().getSelectedItem();
        if(movieFX == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/AdminEditMovie.fxml"));
        Parent root = loader.load();
        AdminEditMovieController adminEditMovieController = loader.getController();
        adminEditMovieController.setMovieManagerController(this, movieFX);
        loadAddMovieWindow(root);
    }

    public void deleteMovie(MouseEvent mouseEvent) throws IOException, SQLException {
        MovieFX movieFX = movieManagerTable.getSelectionModel().getSelectedItem();
        if(movieFX == null) return;
        if(server.deleteMovie(movieFX.getId())) {
            loadMoviesToTable(null);
        }
    }


    private void loadAddMovieWindow(Parent root) throws IOException {
        Scene scene = emptyTableLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) moviesManagerAnchorPane.getParent();
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


