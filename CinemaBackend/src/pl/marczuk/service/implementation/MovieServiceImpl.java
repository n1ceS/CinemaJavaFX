package pl.marczuk.service.implementation;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.types.SqlDateStringType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.query.In;
import pl.marczuk.model.Movie;
import pl.marczuk.service.MovieService;

import java.sql.SQLException;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private Dao<Movie, Integer> movieDao;

    public MovieServiceImpl(Dao<Movie, Integer> movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public boolean addMovie(Movie movie) throws SQLException {
        try {
            movieDao.create(movie);
            return true;
        } catch (SQLException sqlException) {
            System.out.println("Niedodano filmu do bazy danych!");
            return false;
        }
    }

    @Override
    public List<Movie> getAll() {
        try {
            return movieDao.queryForAll();
        } catch (SQLException sqlException) {
            System.out.println("Cant get all movies!");
            return null;
        }
    }

    @Override
    public Movie getMovieDetails(Integer movieId) throws SQLException {
       return movieDao.queryForEq("id", movieId).get(0);
    }

    @Override
    public boolean updateMovie(Movie movie) throws SQLException {
       return movieDao.update(movie) == 1;
    }

}
