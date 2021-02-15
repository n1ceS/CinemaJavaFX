package pl.marczuk.service;

import pl.marczuk.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieService {
    boolean addMovie(Movie movie) throws SQLException;
    List<Movie> getAll();
    Movie getMovieDetails(Integer movieId) throws SQLException;
    boolean updateMovie(Movie movie) throws SQLException;
}
