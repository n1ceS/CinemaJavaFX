package pl.marczuk.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pl.marczuk.Cinema_Login;
import pl.marczuk.model.Session;
import pl.marczuk.service.ServerFacade;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public static Integer USER_ID  = 0;
    ServerFacade server;
    // ZMIENNE DO "PRZESUWANIA" OKNEM
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane mainRootDashboardAnchorPane;

    @FXML
    public Label homeButton;

    @FXML
    public Label moviesButton;

    @FXML
    public Label ordersButton;

    @FXML
    private Session currentSession;

    @FXML
    public Label usernameLabel;
    @FXML
    public StackPane dashBoardStackPane;
    @FXML
    public Label adminPanel;
    @FXML
    public ImageView adminIcon;
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    public Label Close;
    @FXML
    private void handleClose(Event event) {
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
        USER_ID = currentSession.getId();
        usernameLabel.setText(currentSession.getFirstName() + " " + currentSession.getLastName());
        if(currentSession.getRole().equals("admin")) {
            adminIcon.setVisible(true);
            adminPanel.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeWindowDraggable();
    }

    private void makeWindowDraggable() {
        mainRootDashboardAnchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        mainRootDashboardAnchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cinema_Login.getpStage().setX(event.getScreenX() - xOffset);
                Cinema_Login.getpStage().setY(event.getScreenY() - yOffset);
            }
        });
    }

    public void loadMovies(Event event) throws IOException {
        loadWindow("Movies.fxml");
    }


    private void loadWindow(String fxmlName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/pl/marczuk/fxml/" + fxmlName));
        Scene scene = moviesButton.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        dashBoardStackPane.getChildren().clear();
        dashBoardStackPane.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(rootAnchorPane);
            rootAnchorPane = (AnchorPane) root;
        });
        timeline.play();
    }

    public void loadHome(MouseEvent mouseEvent) throws IOException {
        loadWindow("Home.fxml");
    }

    public void loadAdminPanel(MouseEvent mouseEvent) throws IOException {
        loadWindow("AdminPanel.fxml");
    }

    public void loadTickets(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/Tickets.fxml"));
        Parent root = loader.load();
        TicketsController ticketsController = loader.getController();
        ticketsController.loadTicketsToTable(null);
        Scene scene = moviesButton.getScene();

        // przemieszczamy okno
        root.translateYProperty().set(scene.getHeight());
        dashBoardStackPane.getChildren().clear();
        dashBoardStackPane.getChildren().add(root);

        //animacja
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> {
            dashBoardStackPane.getChildren().remove(rootAnchorPane);
            rootAnchorPane = (AnchorPane) root;
        });
        timeline.play();
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/marczuk/fxml/Login.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Kino Sława");
        //USTAWIAM TO JAKO GLOWNE OKNO
        Cinema_Login.setpStage(stage);
        //USTAWIAM SESJE KLIENTA
        stage.initStyle(StageStyle.UNDECORATED);
        handleClose(null);
        stage.show();
    }
}
