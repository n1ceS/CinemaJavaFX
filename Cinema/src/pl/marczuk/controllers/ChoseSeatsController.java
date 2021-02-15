package pl.marczuk.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import pl.marczuk.model.ReservedSeat;
import pl.marczuk.model.Seance;
import pl.marczuk.service.ServerFacade;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChoseSeatsController implements Initializable {

    List<String> tempReservedSeats;
    ServerFacade server;
    int status = -1;
    private Integer userId;
    private Integer seanceId;
    @FXML
    public AnchorPane chooseSeatsAnchorPane;
    @FXML
    GridPane seatsPane;
    @FXML
    public Button bookButton;
    @FXML
    public Label backButton;
    @FXML
    public Label movieTitleLabel;
    @FXML
    public Label dateMovieLabel;
    @FXML
    public Label chosenSeatsLabel;
    @FXML
    private Image redSeat, greenSeat, greySeat;
    @FXML
    public Label errorLabel;
    @FXML
    public Label suceedLabel;
    @FXML
    public StackPane stackPane;
    @FXML
    public JFXButton succedButton;
    @FXML
    public Circle firstCircle;
    @FXML
    public Circle secondCircle;
    @FXML
    public Circle thirdCircle;

    public void addSeat(MouseEvent mouseEvent) {
        Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();
        Integer colIndex = seatsPane.getColumnIndex(clickedNode);
        Integer rowIndex = seatsPane.getRowIndex(clickedNode);

        if (colIndex == null) colIndex = 0;
        if (rowIndex == null) rowIndex = 0;
        ImageView imageView = (ImageView) mouseEvent.getSource();
        if(imageView.getImage() == greySeat) {
            imageView.setImage(greenSeat);
            String value = getCharFromValue(rowIndex)+String.valueOf(colIndex+1);
            tempReservedSeats.add(value);
        }else if(imageView.getImage() == greenSeat) {
            imageView.setImage(greySeat);
            String value = getCharFromValue(rowIndex)+String.valueOf(colIndex+1);
            tempReservedSeats.remove(value);
        }
        displayReservedSeats();
    }

    private void displayReservedSeats() {
        StringBuilder sb = new StringBuilder();
        for(String value : tempReservedSeats) {
            sb.append(value+ " ");
        }
        chosenSeatsLabel.setText(sb.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userId = DashboardController.USER_ID;
        tempReservedSeats = new ArrayList<>();
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        }catch (RemoteException | NotBoundException  remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
    }
    public void loadTakenSeats() throws RemoteException, SQLException {
        List<ReservedSeat> takenSeatsList = server.getReservedSeats(seanceId).getList();
            for(ReservedSeat reservedSeat : takenSeatsList) {
                Integer row = getValueFromChar(reservedSeat.getSeatId().charAt(0));
                Integer col = Character.getNumericValue(reservedSeat.getSeatId().charAt(1)) - 1;
                ImageView takenSeatImageView = (ImageView)seatsPane.getChildren().get(row*5+col);
                if(takenSeatImageView instanceof ImageView) takenSeatImageView.setImage(redSeat);
        }
    }
    public void loadSeance() throws RemoteException {
        Seance seance = server.getDetailsAboutSeance(seanceId);
        movieTitleLabel.setText(seance.getMovie().getTitle());
        dateMovieLabel.setText(seance.getDate() + " " + seance.getStartTime().getHour() + ":" + (seance.getStartTime().getMinute() == 0 ? "00" : seance.getStartTime().getMinute()));
    }
    public void setSeanceId(Integer seanceId) throws RemoteException, SQLException {
        this.seanceId = seanceId;
        loadTakenSeats();
    }

    public void backToMovieDetails(MouseEvent mouseEvent) {
        Scene scene = dateMovieLabel.getScene();

        StackPane dashBoardStackPane = (StackPane) chooseSeatsAnchorPane.getParent();

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(chooseSeatsAnchorPane.translateYProperty(), scene.getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(chooseSeatsAnchorPane);
        });
        timeline.play();
    }
    private char getCharFromValue(Integer value) {
        return  (char) (value +65);
    }
    private int getValueFromChar(char value) {
        return   value - 65;
    }
    public void bookReservation(MouseEvent mouseEvent) throws IOException, InterruptedException {
        if(tempReservedSeats.size() == 0)  {
            displayMessage("WYBIERZ MIEJSCE!");
            return;
        }
        if(server.addReservation(userId,seanceId,tempReservedSeats)){
            successWindow();
        }else{
        displayMessage("BŁAD SERWERA!");
        }

}


    private void displayMessage(String message) {
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

    private void successWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pl/marczuk/fxml/SuccessMessage.fxml"));
        Scene scene = chosenSeatsLabel.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        StackPane parentContainer = (StackPane) chooseSeatsAnchorPane.getParent();
        parentContainer.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(chooseSeatsAnchorPane);
        });
        timeline.play();
    }
}

