package pl.marczuk.service.implementation;

import com.j256.ormlite.dao.Dao;
import pl.marczuk.model.ReservedSeat;
import pl.marczuk.model.Seance;
import pl.marczuk.service.SeanceService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeanceServiceImpl implements SeanceService {
    Dao<Seance, Integer> seanceDao;

    public SeanceServiceImpl(Dao<Seance, Integer> seanceDao) {
        this.seanceDao = seanceDao;
    }

    @Override
    public List<Seance> getAll() {
        try{
           return seanceDao.queryForAll();
        } catch (SQLException sqlException) {
            System.out.println("Error while getting all Seances");
        }
        return null;
    }

    @Override
    public List<Seance> getByDate(LocalDate date) {
       try {
           return seanceDao.queryForAll().stream().filter(seance -> seance.getDate().compareTo(date) == 0).collect(Collectors.toList());
       } catch (SQLException sqlException) {
           sqlException.printStackTrace();
       }
       return new ArrayList<>();
    }

    @Override
    public boolean addSeance(Seance seance) throws SQLException {
        return seanceDao.create(seance) == 1;
    }

    @Override
    public List<ReservedSeat> getTakenSeats(Integer seanceId) {
        try {
            Seance seance = seanceDao.queryForEq("seance_id", seanceId).get(0);
            return (List<ReservedSeat>) seance.getReservedSeatList();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public Seance getSeanceDetails(Integer seanceId) {
        try {
            return seanceDao.queryForEq("id", seanceId).get(0);
        } catch (SQLException sqlException) {
            System.out.println("Error during getting details about Seance");
        }
        return null;
    }

    @Override
    public Integer getFreeSeatsCount(Integer seanceId) {
        try {
            Seance seance = seanceDao.queryForEq("id", seanceId).get(0);
            return 50 - seance.getReservedSeatList().size();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean editSeance(Seance seance) throws SQLException {
        return seanceDao.update(seance) == 1;
    }
}
