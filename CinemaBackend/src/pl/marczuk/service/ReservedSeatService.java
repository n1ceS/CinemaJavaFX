package pl.marczuk.service;

import pl.marczuk.model.ListTransporter;
import pl.marczuk.model.ReservedSeat;

import java.sql.SQLException;
import java.util.List;

public interface ReservedSeatService {
    public List<ReservedSeat> getReservedSeats(Integer seanceId) throws SQLException;
    public void addReservedSeats(List<ReservedSeat> reservedSeats) throws SQLException;
    public List<String> getUserReservedSeats(Integer user_id, Integer seance_id)throws SQLException;
}
