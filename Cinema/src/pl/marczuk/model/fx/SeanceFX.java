package pl.marczuk.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalTime;


public class SeanceFX {
    private SimpleIntegerProperty id;
    private ObjectProperty<ImageView> movieImage;
    private SimpleStringProperty movieName, category, date, freeSeats;
    LocalTime seanceStartTime;
    LocalDate seanceDate;

    private  Integer movieId;
    public ObjectProperty<ImageView> getMovieImage() {
        return movieImage;
    }
    public SeanceFX(Integer id, String movieName, Integer movieId, String category, LocalDate seanceDate, LocalTime seanceStartTime, String freeSeats, ImageView movieImage) {
        this.id = new SimpleIntegerProperty(id);
        this.movieName = new SimpleStringProperty(movieName);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(seanceDate.toString() + " " + seanceStartTime.getHour() + ":" +(seanceStartTime.getMinute() == 0 ? "00" : seanceStartTime.getMinute()));
        this.freeSeats = new SimpleStringProperty(freeSeats);
        this.movieImage = new SimpleObjectProperty<>(movieImage);
        this.movieId = movieId;
        this.seanceDate = seanceDate;
        this.seanceStartTime = seanceStartTime;
    }

    public int getId() {
        return id.get();
    }

    public String getMovieName() {
        return movieName.get();
    }

    public ImageView getMovieImageProperty() {
        return movieImage.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getFreeSeats() {
        return freeSeats.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty movieNameProperty() {
        return movieName;
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public ObjectProperty<ImageView> movieImageProperty() {
        return movieImage;
    }

    public SimpleStringProperty freeSeatsProperty() {
        return freeSeats;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setMovieImage(ImageView movieImage) {
        this.movieImage.set(movieImage);
    }

    public void setMovieName(String movieName) {
        this.movieName.set(movieName);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setFreeSeats(String freeSeats) {
        this.freeSeats.set(freeSeats);
    }
    public Integer getMovieId() {
        return movieId;
    }

    public LocalTime getSeanceStartTime() {
        return seanceStartTime;
    }

    public LocalDate getSeanceDate() {
        return seanceDate;
    }
}
