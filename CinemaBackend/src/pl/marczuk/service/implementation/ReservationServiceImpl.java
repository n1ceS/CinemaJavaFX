package pl.marczuk.service.implementation;

import com.j256.ormlite.dao.Dao;
import pl.marczuk.model.Reservation;
import pl.marczuk.model.ReservedSeat;
import pl.marczuk.service.ReservationService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    Dao<Reservation, Integer> reservationDao;

    public ReservationServiceImpl(Dao<Reservation, Integer> reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public void addReservarion(Reservation reservation) {
        try {
            reservationDao.create(reservation);
        } catch (SQLException sqlException) {
            System.out.println("Error while adding new reservation");
        }
    }

    @Override
    public Reservation get(Integer id) {
        try {
            return reservationDao.queryForEq("id", id).get(0);
        }catch (SQLException sqlException) {
            System.out.println("Error while getting reservation with id " + id);
        }
        return null;
    }

    @Override
    public List<Reservation> getUserReservations(Integer id) {
        try {
            return reservationDao.queryForEq("user_id", id);
        }catch (SQLException sqlException) {
            System.out.println("Error while getting user reservation with id: " + id);
        }
        return null;
    }

}
