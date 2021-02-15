package pl.marczuk.controllers;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.Movie;
import pl.marczuk.service.ServerFacade;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MovieDetailsController implements Initializable {
    public static String IMAGE_SRC = "src\\pl\\marczuk\\images\\dashboard\\movies\\";
    private Integer seanceId;
    private int movieId;
    Movie movie;
    ServerFacade server;
    LocalDate seanceDate;
    LocalTime seanceStartTime;


    @FXML
    public ImageView movieImage;
    @FXML
    public Label titleLabel;
    @FXML
    public Label yearLabel;
    @FXML
    public Label lengthLabel;
    @FXML
    public JFXTextArea descriptionTextArea;
    @FXML
    public Button bookTicket;
    @FXML
    public Label categoryLabel;
    @FXML
    public Label backButton;
    @FXML
    public Label dateLabel;
    @FXML
    public Label freeSeatsLabel;
    @FXML
    public AnchorPane movieDetailsAnchorPane;
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

    public void setDetails(int movieId, LocalDate seanceDate, LocalTime seanceStartTime, String freeSeats, Integer seanceId) throws RemoteException, SQLException {
        this.seanceDate = seanceDate;
        this.seanceStartTime = seanceStartTime;
        this.seanceId = seanceId;
        this.movieId = movieId;
        movie = server.getMovieDetails(movieId);
        titleLabel.setText(movie.getTitle());
        yearLabel.setText(movie.getYear().toString());
        lengthLabel.setText(movie.getLength().toString()  + " minut");
        dateLabel.setText(seanceDate.toString()+ " " + seanceStartTime.getHour()+":" +(seanceStartTime.getMinute() == 0 ? "00" : seanceStartTime.getMinute()));
        categoryLabel.setText(movie.getCategory());
        descriptionTextArea.setText(movie.getDescription());
        freeSeatsLabel.setText("POŚPIESZ SIĘ, ZOSTAŁO " +  freeSeats +  " MIEJSC!");
        //SPRAWDZENIE, CZY SEANS SIĘ JUZ ODBYL
        if(seanceDate.compareTo(LocalDate.now()) < 0) {
            freeSeatsLabel.setText("TEN SEANS NIE JEST JUŻ DOSTĘPNY!");
            bookTicket.setDisable(true);
        }else if(seanceDate.compareTo(LocalDate.now()) == 0 ) {
            if(seanceStartTime.compareTo(LocalTime.now()) < 0) {
                freeSeatsLabel.setText("TEN SEANS NIE JEST JUŻ DOSTĘPNY!");
                bookTicket.setDisable(true);
            }
        }
        File file = new File(IMAGE_SRC + movieId + ".jpg");
        Image image = new Image(file.toURI().toString(), 200, 300, false, false);
        movieImage.setImage(image);

    }


    public void backToMovies(MouseEvent mouseEvent) {
        Scene scene = freeSeatsLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) movieDetailsAnchorPane.getParent();

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(movieDetailsAnchorPane.translateYProperty(), scene.getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(movieDetailsAnchorPane);
        });
        timeline.play();
    }

    public void loadReservation(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/ChoseSeats.fxml"));
        Parent root = loader.load();
        ChoseSeatsController choseSeatsController = loader.getController();
        choseSeatsController.setSeanceId(seanceId);
        choseSeatsController.loadSeance();
        loadWindow(root);

    }

    private void loadWindow(Parent root) throws IOException {

        StackPane dashBoardStackPane = (StackPane) movieDetailsAnchorPane.getParent();
        Scene scene = categoryLabel.getScene();
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
