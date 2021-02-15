package pl.marczuk.controllers;

import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.marczuk.model.Movie;
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
import java.util.ResourceBundle;

public class AdminEditMovieController implements Initializable {
    public static String IMAGE_SRC = "src\\pl\\marczuk\\images\\dashboard\\movies\\";
    ServerFacade server;
    @FXML
    public JFXTextArea descriptionTextArea;
    @FXML
    public TextField categoryTextField;
    @FXML
    public TextField lengthTextField;
    @FXML
    public TextField yearTextField;
    @FXML
    public TextField titleTextField;
    @FXML
    public AnchorPane editMovieAnchorPane;
    @FXML
    public ImageView movieImage;
    @FXML
    public Label choosedImageLabel;
    @FXML
    public Label titleErrorLabel;
    @FXML
    public Label yearErrorLabel;
    @FXML
    public Label lengthErrorLabel;
    @FXML
    public Label categoryErrorLabel;
    @FXML
    public Label imageAndTextAreaErrorLabel;
    File file;
    private MoviesManagerController movieManagerController;
    MovieFX movieFX;

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
    public void setMovieManagerController(MoviesManagerController moviesManagerController, MovieFX movieToEdit) {
        this.movieFX = movieToEdit;
        this.movieManagerController = moviesManagerController;
        titleTextField.setText(movieToEdit.getMovieName());
        yearTextField.setText(movieToEdit.getYear());
        lengthTextField.setText(movieToEdit.getLength().split(" ")[0]);
        categoryTextField.setText(movieToEdit.getCategory());
        descriptionTextArea.setText(movieToEdit.getDescription());
    }
    public void loadMovieImage(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Pliki Obrazów", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Wybierz ikonę filmu");
        Stage stage = (Stage)  categoryTextField.getScene().getWindow();

        file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            Image choosedImage = new Image(file.toURI().toString());
            movieImage.setImage(choosedImage);
            choosedImageLabel.setVisible(true);
        }
    }

    public void editMovie(MouseEvent mouseEvent) throws IOException, SQLException {
        if(validateInput()) {
            if(file ==null) file = new File(IMAGE_SRC + movieFX.getId() + ".jpg");
            Movie movie = new Movie(titleTextField.getText(), descriptionTextArea.getText(), Integer.parseInt(yearTextField.getText()), Integer.parseInt(lengthTextField.getText()), ImageConverter.imgToByte(file),categoryTextField.getText());
            movie.setId(movieFX.getId());
            if(server.updateMovie(movie)) {
                backToMovies(null);
                movieManagerController.loadMoviesToTable(null);
            }
        }
    }
    public boolean validateInput() {
        if(titleTextField.getText().length() == 0) {
            displayMessage(titleErrorLabel, "Pole Tytuł nie może być puste!");
            return false;
        }
        if(yearTextField.getText().length() == 0) {
            displayMessage(yearErrorLabel, "Pole Rok nie może być puste!");
            return false;
        }else {
            try{
                int year = Integer.parseInt(yearTextField.getText());
                if(year < 1900 || year >2021) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                displayMessage(yearErrorLabel, "Rok musi być liczbą z przedziału 1900-2021!");
                return false;
            }
        }
        if(lengthTextField.getText().length() == 0) {
            displayMessage(lengthErrorLabel, "Pole Długość filmu nie może być puste!");
            return false;
        }else {
            try{
                int length = Integer.parseInt(lengthTextField.getText());
                if(length <=0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                displayMessage(lengthErrorLabel, "Długość filmu musi być liczbą większą od zera!");
                return false;
            }
        }
        if(categoryTextField.getText().length() == 0) {
            displayMessage(categoryErrorLabel, "Pole Kategoria nie może być puste!");
            return false;
        }
        if(descriptionTextArea.getText().length() == 0) {
            displayMessage(imageAndTextAreaErrorLabel, "Pole Opis nie może być puste!");
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


    public void backToMovies(MouseEvent mouseEvent) {
        Scene scene = yearErrorLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) editMovieAnchorPane.getParent();

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(editMovieAnchorPane.translateYProperty(), scene.getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(editMovieAnchorPane);
        });
        timeline.play();
    }
}
