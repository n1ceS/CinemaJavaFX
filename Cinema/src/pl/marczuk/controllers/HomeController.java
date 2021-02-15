package pl.marczuk.controllers;

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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pl.marczuk.model.Seance;
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

public class HomeController implements Initializable {
    public static String IMAGE_SRC = "src\\pl\\marczuk\\images\\dashboard\\";

    ServerFacade server;
    List<Seance> seanceList;
    @FXML
    public ImageView promotedMovie1Image;
    @FXML
    public Label promotedMovie1Title;
    @FXML
    public Label promotedMovie1Category;
    @FXML
    public Label promotedMovie1FreeSeats;
    @FXML
    public Label promotedMovie1Date;
    @FXML
    public Button bookTicketPromoted1;

    @FXML
    public ImageView promotedMovie2Image;
    @FXML
    public Label promotedMovie2Title;
    @FXML
    public Label promotedMovie2Category;
    @FXML
    public Label promotedMovie2FreeSeats;
    @FXML
    public Label promotedMovie2Date;
    @FXML
    public Button bookTicketPromoted2;

    @FXML
    public Label promotedMovie3Title;
    @FXML
    public Label promotedMovie3Category;
    @FXML
    public Button bookTicketPromoted3;
    @FXML
    public ImageView promotedMovie3Image;
    @FXML
    public Label promotedMovie3FreeSeats;
    @FXML
    public Label promotedMovie3Date;
    @FXML
    public AnchorPane dashBoardAnchorPane;


    public void loadPromotedSeances() throws IOException {
        seanceList = server.getPromotedSeances().getList();
        //Seans 1
        for(int i = 0; i < 3; ++i) {
            ImageConverter.byteToJpg(seanceList.get(i).getMovie().getImage(),"promoted_movie"+(i+1)+".jpg");
        }
        Seance seance1 = seanceList.get(0);
        Seance seance2 = seanceList.get(1);
        Seance seance3 = seanceList.get(2);
        //WOLNE MIEJSCA
        int freeSeats1 = server.getFreeSeats(seance1.getId());
        int freeSeats2 = server.getFreeSeats(seance2.getId());
        int freeSeats3 = server.getFreeSeats(seance3.getId());

        File file = new File(IMAGE_SRC + "promoted_movie1.jpg");
        promotedMovie1Image.setImage(new Image(file.toURI().toString()));
        file = new File(IMAGE_SRC + "promoted_movie2.jpg");
        promotedMovie2Image.setImage(new Image(file.toURI().toString()));
        file = new File(IMAGE_SRC + "promoted_movie3.jpg");
        promotedMovie3Image.setImage(new Image(file.toURI().toString()));

        promotedMovie1Category.setText(seance1.getMovie().getCategory());
        promotedMovie2Category.setText(seance2.getMovie().getCategory());
        promotedMovie3Category.setText(seance3.getMovie().getCategory());

        promotedMovie1Date.setText(seance1.getDate().toString() + " " + seance1.getStartTime().getHour() +":"+ (seance1.getStartTime().getMinute() ==0 ? "00" : seance1.getStartTime().getMinute()));
        promotedMovie2Date.setText(seance2.getDate().toString() + " " + seance2.getStartTime().getHour() +":"+ (seance2.getStartTime().getMinute() ==0 ? "00" : seance2.getStartTime().getMinute()));
        promotedMovie3Date.setText(seance3.getDate().toString() + " " + seance3.getStartTime().getHour() +":"+ (seance3.getStartTime().getMinute() ==0 ? "00" : seance3.getStartTime().getMinute()));

        promotedMovie1FreeSeats.setText(String.valueOf(freeSeats1));
        promotedMovie2FreeSeats.setText(String.valueOf(freeSeats2));
        promotedMovie3FreeSeats.setText(String.valueOf(freeSeats3));

        promotedMovie1Title.setText(seance1.getMovie().getTitle()+ " (" +seance1.getMovie().getYear() + ")");
        promotedMovie2Title.setText(seance2.getMovie().getTitle()+ " (" +seance2.getMovie().getYear() + ")");
        promotedMovie3Title.setText(seance3.getMovie().getTitle()+ " (" +seance3.getMovie().getYear() + ")");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        }catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
        try {
            loadPromotedSeances();
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadDetails(MouseEvent mouseEvent) throws IOException, SQLException {
        if(((Control)mouseEvent.getSource()).getId().equals("bookTicketPromoted1")) {
            getDetails(0);
        }else if(((Control)mouseEvent.getSource()).getId().equals("bookTicketPromoted2")) {
            getDetails(1);
        }else {
            getDetails(2);
        }
    }
    private void getDetails(int index) throws IOException, SQLException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/MovieDetails.fxml"));
            Parent root = loader.load();
            MovieDetailsController movieDetailsController = loader.getController();
            movieDetailsController.setDetails(seanceList.get(index).getMovie().getId(), seanceList.get(index).getDate(),seanceList.get(index).getStartTime(), server.getFreeSeats(seanceList.get(index).getId()).toString(), seanceList.get(index).getId());
            loadMovieDetailsWindow(root);

    }

    private void loadMovieDetailsWindow(Parent root) throws IOException {
        Scene scene = promotedMovie1Category.getScene();

        StackPane dashBoardStackPane = (StackPane) dashBoardAnchorPane.getParent();
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
