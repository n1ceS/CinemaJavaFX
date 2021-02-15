package pl.marczuk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import pl.marczuk.model.ListTransporter;
import pl.marczuk.model.Reservation;
import pl.marczuk.model.fx.TicketFX;
import pl.marczuk.service.ServerFacade;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TicketsController implements Initializable {

    private ObservableList<TicketFX> tickets = FXCollections.observableArrayList();
    ServerFacade server;
    @FXML
    public AnchorPane ticketsAnchorPane;
    @FXML
    public ImageView emptyTableImage;
    @FXML
    public Label emptyTableLabel;
    @FXML
    public TableView<TicketFX> ticketsTable;
    public TableColumn<TicketFX, String> titleMovie;
    public TableColumn<TicketFX, String> lengthMovie;
    public TableColumn<TicketFX, String> dateMovie;
    public TableColumn<TicketFX, String> selectedSeats;
    public TableColumn<TicketFX, Integer> movieId;
    public TableColumn<TicketFX, String> status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerFacade) registry.lookup("CinemaService");
        } catch (RemoteException | NotBoundException remoteException) {
            System.out.println("Błąd połączenia z serwerem!");
            return;
        }
        lengthMovie.setCellValueFactory(new PropertyValueFactory<>("lengthMovie"));
        selectedSeats.setCellValueFactory(new PropertyValueFactory<>("selectedSeats"));
        dateMovie.setCellValueFactory(new PropertyValueFactory<>("dateMovie"));
        movieId.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        titleMovie.setCellValueFactory(new PropertyValueFactory<>("titleMovie"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        //USTAWIAM BRAK WIADOMOSCI W PRZYPADKU PUSTEJ TABELI
        ticketsTable.setPlaceholder(new Label(""));
    }
    public void loadTicketsToTable(ActionEvent event) throws IOException, SQLException {
        tickets.removeAll();
        ticketsTable.getItems().clear();
        ListTransporter reservationFromServer = server.getAllOrders(DashboardController.USER_ID);
        List<Reservation> reservationList = reservationFromServer.getList();
        if(reservationList.size() == 0 ) {
            emptyTableImage.setVisible(true);
            emptyTableLabel.setVisible(true);
        }else {
            emptyTableImage.setVisible(false);
            emptyTableLabel.setVisible(false);
        }
        for (Reservation reservation : reservationList) {
            String reservedSeats = String.join(",",server.getUserReservedSeats(DashboardController.USER_ID, reservation.getSeance().getId()).getList());
            String date = reservation.getSeance().getDate() + " " + reservation.getSeance().getStartTime().getHour() +":" + (reservation.getSeance().getStartTime().getMinute() == 0 ? "00" : (reservation.getSeance().getStartTime().getMinute()));
            TicketFX ticketFX = new TicketFX(reservation.getId(),reservation.getSeance().getMovie().getTitle(), date,reservedSeats, String.valueOf(reservation.getSeance().getMovie().getLength()), reservation.getStatus());
            tickets.add(ticketFX);
        }
        ticketsTable.setItems(tickets);
    }
}
