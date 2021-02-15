package pl.marczuk.service.implementation;

import com.j256.ormlite.dao.Dao;
import pl.marczuk.model.User;
import pl.marczuk.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl  implements UserService {
    Dao<User,Integer> userDao;
    public UserServiceImpl(Dao<User, Integer> userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        userDao.create(user);
        return true;
    }

    @Override
    public User signIn(String username, String password) throws SQLException {
        List<User> userList = userDao.queryForEq("email", username);
        if(userList.size() == 0 ) return  null;
        if(userList.get(0).getPassword().equals(password)) return userList.get(0);
        return null;
    }

    @Override
    public boolean checkIfUserExists(String email) {
        try {
            if (userDao.queryForEq("email", email).size() == 0)
                return false;
            else return true;
        } catch (SQLException sqlException) {
            System.out.println("Error occured while finding Email user");
        }
        return true;
    }


}
