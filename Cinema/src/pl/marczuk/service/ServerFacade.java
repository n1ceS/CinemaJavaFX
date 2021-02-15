package pl.marczuk.service;

import pl.marczuk.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ServerFacade extends Remote {
    public Session signIn(String email, String password) throws RemoteException, SQLException;

    public boolean signUp(String email, String password, String firstName, String lastName) throws RemoteException, SQLException;

    public boolean addReservation(Integer userId, Integer seanceId, List<String> reservedSeatList) throws RemoteException;

    public Integer getFreeSeats(Integer seanceId) throws RemoteException;

    public ListTransporter getAllOrders(Integer userId) throws RemoteException;

    public Seance getDetailsAboutSeance(Integer seanceId) throws RemoteException;

    public ListTransporter getPromotedSeances() throws RemoteException;

    public boolean checkMail(String mail) throws RemoteException;


    public ListTransporter getByDate(LocalDate date) throws RemoteException;

    public Movie getMovieDetails(Integer movieID) throws RemoteException, SQLException;

    public ListTransporter getReservedSeats(Integer seanceId) throws RemoteException, SQLException;

    public ListTransporter getAllMovies() throws RemoteException;

    public boolean addMovie(Movie movie) throws RemoteException, SQLException;

    public boolean updateMovie(Movie movie) throws RemoteException, SQLException;

    public boolean deleteMovie(Integer movie_id) throws RemoteException, SQLException;

    public long getUsersCount() throws RemoteException, SQLException;

    public long getMoviesCount() throws RemoteException, SQLException;

    public long getReservationsCount() throws RemoteException, SQLException;

    public ListTransporter getAllSeances() throws RemoteException, SQLException;

    public boolean addSeance(Seance seance) throws RemoteException, SQLException;

    boolean editSeance(Seance seance) throws RemoteException, SQLException;

    boolean deleteSeance(int id) throws RemoteException, SQLException;

    ListTransporter getUserReservedSeats(int user_id, int seance_id) throws RemoteException, SQLException;
}
