package pl.marczuk.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;


public class SeanceManagerFX {
    private SimpleIntegerProperty id;
    private SimpleStringProperty titleMovie;
    private ObjectProperty<LocalTime> hourSeance;
    private ObjectProperty<LocalDate> dateSeance;



    private  Integer movieId;
    public SeanceManagerFX(Integer id, String movieName, Integer movieId, LocalDate seanceDate, LocalTime seanceStartTime) {
        this.id = new SimpleIntegerProperty(id);
        this.titleMovie = new SimpleStringProperty(movieName);
        this.movieId = movieId;
        this.dateSeance = new SimpleObjectProperty<>(seanceDate);
        this.hourSeance = new SimpleObjectProperty<>(seanceStartTime);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitleMovie() {
        return titleMovie.get();
    }

    public SimpleStringProperty titleMovieProperty() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie.set(titleMovie);
    }

    public LocalTime getHourSeance() {
        return hourSeance.get();
    }

    public ObjectProperty<LocalTime> hourSeanceProperty() {
        return hourSeance;
    }

    public void setHourSeance(LocalTime hourSeance) {
        this.hourSeance.set(hourSeance);
    }

    public LocalDate getDateSeance() {
        return dateSeance.get();
    }

    public ObjectProperty<LocalDate> dateSeanceProperty() {
        return dateSeance;
    }

    public void setDateSeance(LocalDate dateSeance) {
        this.dateSeance.set(dateSeance);
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
