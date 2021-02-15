package pl.marczuk.service;

import pl.marczuk.model.ReservedSeat;
import pl.marczuk.model.Seance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SeanceService {
    List<Seance> getAll();
    List<Seance> getByDate(LocalDate date);
    boolean addSeance(Seance seance) throws SQLException;
    List<ReservedSeat> getTakenSeats(Integer seanceId);

    Seance getSeanceDetails(Integer seanceId);
    Integer getFreeSeatsCount(Integer seanceId);

    boolean editSeance(Seance seance) throws SQLException;
}
