package pl.marczuk.util;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.types.SqlDateStringType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import pl.marczuk.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DatabaseManager {
    public static final String DATABASE_URL ="jdbc:sqlite:C:\\Users\\Michal\\IdeaProjects\\CinemaBackend\\src\\pl\\marczuk\\cinema.db" ;
    ConnectionSource connectionSource;
    private Dao<User, Integer> userDao;
    private Dao<Seance, Integer> seanceDao;
    private Dao<Movie, Integer> movieDao;
    private Dao<Reservation, Integer> reservationDao;
    private Dao<ReservedSeat, Integer> reservedSeatDao;

    public DatabaseManager() {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
        } catch (SQLException sqlException) {
            System.out.println("Brak połączenia z bazą danych");
            System.exit(1);
        }
    }
        public void test () throws SQLException, IOException {
            //TableUtils.createTable(connectionSource, User.class);
            //TableUtils.createTable(connectionSource, Movie.class);
            //TableUtils.createTable(connectionSource, Seance.class);
            //TableUtils.createTable(connectionSource, ReservedSeat.class);
            TableUtils.createTable(connectionSource, Reservation.class);
//            byte[] image = ImageConverter.imgToByte("src\\pl\\marczuk\\images\\promoted_movie.jpg");
//            Movie movie = new Movie("Titanic","Co ma wisieć nie utonie", 1997, 90, image, "Romans/Dramat");
//            image = ImageConverter.imgToByte("src\\pl\\marczuk\\images\\promoted_movie2.jpg");
//            movie = new Movie("Gdzie jest Nemo?","Rybka lubi pływać", 2003, 150, image, "Familijny/Przygodowy");
//            movieDao.create(movie);
//            image = ImageConverter.imgToByte("src\\pl\\marczuk\\images\\promoted_movie3.jpg");
//            movie = new Movie("Więcej niż myślisz","Nie myśl tak dużo bo zostaniesz myśliwym.", 2020, 300, image, "Komediodramat");
//
//           movieDao.create(movie);
//            movieDao = DaoManager.createDao(connectionSource, Movie.class);
//            seanceDao = DaoManager.createDao(connectionSource, Seance.class);
//            Seance seance = new Seance(movieDao.queryForEq("id", 1).get(0), LocalTime.now(), LocalDate.now());
//            //seanceDao.create(seance);
//            //seance = new Seance(movieDao.queryForEq("id", 2).get(0), LocalTime.now(), LocalDate.now());
//            //seanceDao.create(seance);
//            seance = new Seance(movieDao.queryForEq("id", 3).get(0), LocalTime.now(), LocalDate.now());
//            seanceDao.create(seance);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public Dao<User, Integer> getUserDao() throws SQLException {
        userDao = DaoManager.createDao(connectionSource, User.class);
        return userDao;
    }

    public Dao<Seance, Integer> getSeanceDao() throws SQLException {
        seanceDao = DaoManager.createDao(connectionSource, Seance.class);
        return seanceDao;
    }

    public Dao<Movie, Integer> getMovieDao() throws SQLException {
        movieDao = DaoManager.createDao(connectionSource, Movie.class);
        return movieDao;
    }

    public Dao<Reservation, Integer> getReservationDao() throws SQLException {
        reservationDao = DaoManager.createDao(connectionSource, Reservation.class);
        return reservationDao;
    }
    public Dao<ReservedSeat, Integer> getReservedSeatDao() throws SQLException {
        reservedSeatDao = DaoManager.createDao(connectionSource, ReservedSeat.class);
        return reservedSeatDao;
    }
}
