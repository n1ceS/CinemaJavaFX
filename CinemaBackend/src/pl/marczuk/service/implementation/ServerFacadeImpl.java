package pl.marczuk.service.implementation;

import pl.marczuk.model.*;
import pl.marczuk.service.*;
import pl.marczuk.util.DatabaseManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerFacadeImpl extends UnicastRemoteObject implements ServerFacade {
    static DatabaseManager databaseManager;

    UserService userService;
    MovieService movieService;
    ReservationService reservationService;
    SeanceService seanceService;
    ReservedSeatService reservedSeatService;

    public ServerFacadeImpl(DatabaseManager databaseManager) throws RemoteException, SQLException {
        this.databaseManager = databaseManager;
        userService = new UserServiceImpl(databaseManager.getUserDao());
        movieService = new MovieServiceImpl(databaseManager.getMovieDao());
        reservationService = new ReservationServiceImpl(databaseManager.getReservationDao());
        seanceService = new SeanceServiceImpl(databaseManager.getSeanceDao());
        reservedSeatService = new ReservedSeatServiceImpl(databaseManager.getReservedSeatDao());
    }

    @Override
    public Session signIn(String email, String password) throws SQLException, RemoteException {
        User user = userService.signIn(email, password);
        if( user!= null){
            return new Session(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
        }
        return null;
    }

    @Override
    public boolean signUp(String email, String password, String firstName, String lastName) throws SQLException,RemoteException {
        return userService.addUser(new User(firstName,lastName,email, password));
    }

    @Override
    public boolean addReservation(Integer userId, Integer seanceId, List<String> reservedSeatList) throws RemoteException{
        try {
            User user = databaseManager.getUserDao().queryForEq("id", userId).get(0);
            Seance seance = databaseManager.getSeanceDao().queryForEq("id", seanceId).get(0);
            List<ReservedSeat> reservedSeats = new ArrayList<>();
            Reservation reservation = new Reservation(user, seance);
            for(String item : reservedSeatList) {
                reservedSeats.add(new ReservedSeat(reservation, seance, user, item));
            }
            reservationService.addReservarion(reservation);
            reservedSeatService.addReservedSeats(reservedSeats);
            EmailService.sendEmail(user, reservation, reservedSeatList);
            return true;
        } catch (SQLException sqlException) {
            System.out.println("Error occured during adding reservation ");
        }
        return false;
    }

    @Override
    public Integer getFreeSeats(Integer seanceId) throws RemoteException{
       return seanceService.getFreeSeatsCount(seanceId);
    }

    @Override
    public ListTransporter getAllOrders(Integer userId) throws RemoteException {
        return new ListTransporter(reservationService.getUserReservations(userId));
    }

    @Override
    public Seance getDetailsAboutSeance(Integer seanceId) throws RemoteException{
        return seanceService.getSeanceDetails(seanceId);
    }

    @Override
    public ListTransporter getPromotedSeances() throws RemoteException {
        List<Seance> promotedSeanceList = new ArrayList<>();
        int size = 0;
        try {
            List<Seance> seanceList = databaseManager.getSeanceDao().queryForAll();
            for(Seance seanceItem : seanceList) {
                if (seanceItem.getDate().compareTo(LocalDate.now()) >= 0 && size < 3) {
                    size++;
                    promotedSeanceList.add(seanceItem);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new ListTransporter<>(promotedSeanceList);
    }

    @Override
    public boolean checkMail(String mail) throws RemoteException {
        return userService.checkIfUserExists(mail);
    }

    @Override
    public ListTransporter getByDate(LocalDate date) {
        return new ListTransporter<>(seanceService.getByDate(date));
    }

    @Override
    public Movie getMovieDetails(Integer movieID) throws RemoteException, SQLException {
       return  movieService.getMovieDetails(movieID);
    }

    @Override
    public ListTransporter getReservedSeats(Integer seanceId) throws RemoteException, SQLException {
        return  new ListTransporter(reservedSeatService.getReservedSeats(seanceId));
    }

    @Override
    public ListTransporter getAllMovies() throws RemoteException {
        return new ListTransporter(movieService.getAll());
    }

    @Override
    public boolean addMovie(Movie movie) throws RemoteException, SQLException {
        return movieService.addMovie(movie);
    }

    @Override
    public boolean updateMovie(Movie movie) throws RemoteException, SQLException {
        return movieService.updateMovie(movie);
    }

    @Override
    public boolean deleteMovie(Integer movie_id) throws RemoteException, SQLException {
        Movie movie = movieService.getMovieDetails(movie_id);
        List<Seance> seanceList = databaseManager.getSeanceDao().queryForEq("movie_id", movie);
        for(Seance seance: seanceList) {
            List<Reservation> reservationList = databaseManager.getReservationDao().queryForEq("seance_id", seance);
            for(Reservation reservation : reservationList) {
                databaseManager.getReservationDao().delete(reservation);
            }
            List<ReservedSeat> reservedSeatList = databaseManager.getReservedSeatDao().queryForEq("seance_id", seance);
            for(ReservedSeat reservedSeat : reservedSeatList) {
                databaseManager.getReservedSeatDao().delete(reservedSeat);
            }
            databaseManager.getSeanceDao().delete(seance);
        }
        databaseManager.getMovieDao().delete(movie);
        return true;
    }

    @Override
    public long getUsersCount() throws RemoteException, SQLException {
        return databaseManager.getUserDao().queryForAll().stream().count();
    }

    @Override
    public long getMoviesCount() throws RemoteException, SQLException {
        return movieService.getAll().stream().count();
    }

    @Override
    public long getReservationsCount() throws RemoteException, SQLException {
        return databaseManager.getReservationDao().queryForAll().stream().count();
    }

    @Override
    public ListTransporter getAllSeances() throws RemoteException, SQLException {
        return  new ListTransporter(seanceService.getAll());
    }

    @Override
    public boolean addSeance(Seance seance) throws RemoteException, SQLException {
        return seanceService.addSeance(seance);
    }

    @Override
    public boolean editSeance(Seance seance) throws RemoteException, SQLException {
        return seanceService.editSeance(seance);
    }

    @Override
    public boolean deleteSeance(int seance_id) throws RemoteException, SQLException {
        Seance seance = seanceService.getSeanceDetails(seance_id);

        List<ReservedSeat> reservedSeatList = databaseManager.getReservedSeatDao().queryForEq("seance_id", seance);
        for(ReservedSeat reservedSeat: reservedSeatList) {
                databaseManager.getReservedSeatDao().delete(reservedSeat);
            }

        List<Reservation> reservationList = databaseManager.getReservationDao().queryForEq("seance_id", seance);
        for(Reservation reservation : reservationList) {
            databaseManager.getReservationDao().delete(reservation);
        }

        databaseManager.getSeanceDao().delete(seance);
        return true;
    }

    @Override
    public ListTransporter getUserReservedSeats(int user_id, int seance_id) throws RemoteException, SQLException {
        return new ListTransporter(reservedSeatService.getUserReservedSeats(user_id,seance_id));
    }

}
