package pl.marczuk.service.implementation;

import com.j256.ormlite.dao.Dao;
import pl.marczuk.model.ReservedSeat;
import pl.marczuk.model.Seance;
import pl.marczuk.service.ReservedSeatService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservedSeatServiceImpl implements ReservedSeatService {
    Dao<ReservedSeat, Integer> reservedSeatsDao;
    public ReservedSeatServiceImpl(Dao<ReservedSeat, Integer> reservedSeatsDao) {
        this.reservedSeatsDao = reservedSeatsDao;
    }
    @Override
    public List<ReservedSeat> getReservedSeats(Integer seanceId) throws SQLException {
        return reservedSeatsDao.queryForEq("seance_id", seanceId);
    }

    @Override
    public void addReservedSeats(List<ReservedSeat> reservedSeats) throws SQLException {
        for(ReservedSeat reservedSeatItem : reservedSeats) {
            reservedSeatsDao.create(reservedSeatItem);
        }
    }
    @Override
    public List<String> getUserReservedSeats(Integer user_id, Integer seance_id)throws SQLException {
        List<ReservedSeat> reservedSeatList = reservedSeatsDao.queryForEq("seance_id" ,seance_id);
        List<String> seats = new ArrayList<>();
        for(ReservedSeat reservedSeat : reservedSeatList) {
            if(reservedSeat.getSeatOwner().getId() == user_id) {
                seats.add(reservedSeat.getSeatId());
            }
        }
        return seats;
    }
}
