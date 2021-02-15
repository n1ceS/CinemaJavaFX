package pl.marczuk.service;

import pl.marczuk.model.Reservation;
import pl.marczuk.model.ReservedSeat;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService {

    void addReservarion(Reservation reservation);
    Reservation get(Integer id);
    List<Reservation> getUserReservations(Integer id);
}
