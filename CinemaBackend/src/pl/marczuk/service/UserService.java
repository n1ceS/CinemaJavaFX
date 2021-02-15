package pl.marczuk.service;

import pl.marczuk.model.User;

import java.sql.SQLException;

public interface UserService {

    boolean addUser(User user) throws SQLException;

    User signIn(String username, String password) throws SQLException;

    boolean checkIfUserExists(String email);
    
}
