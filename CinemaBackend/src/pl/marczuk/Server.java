package pl.marczuk;

import pl.marczuk.service.ServerFacade;
import pl.marczuk.service.implementation.ServerFacadeImpl;
import pl.marczuk.util.DatabaseManager;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws IOException, SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        //databaseManager.test();
        ServerFacade serverFacade = new ServerFacadeImpl(databaseManager);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("CinemaService", serverFacade);

    }
}
