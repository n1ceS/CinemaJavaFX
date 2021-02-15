package pl.marczuk.model.fx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TicketFX {
        private SimpleIntegerProperty id;
        private SimpleStringProperty titleMovie, dateMovie, selectedSeats, lengthMovie, status;

        public TicketFX(Integer id, String titleMovie, String dateMovie, String selectedSeats, String lengthMovie, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.titleMovie = new SimpleStringProperty(titleMovie);
            this.dateMovie = new SimpleStringProperty(dateMovie);
            this.selectedSeats = new SimpleStringProperty(selectedSeats);
            this.lengthMovie = new SimpleStringProperty(lengthMovie + " min.");
            this.status = new SimpleStringProperty(status);
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

    public String getDateMovie() {
        return dateMovie.get();
    }

    public SimpleStringProperty dateMovieProperty() {
        return dateMovie;
    }

    public void setDateMovie(String dateMovie) {
        this.dateMovie.set(dateMovie);
    }

    public String getSelectedSeats() {
        return selectedSeats.get();
    }

    public SimpleStringProperty selectedSeatsProperty() {
        return selectedSeats;
    }

    public void setSelectedSeats(String selectedSeats) {
        this.selectedSeats.set(selectedSeats);
    }

    public String getLengthMovie() {
        return lengthMovie.get();
    }

    public SimpleStringProperty lengthMovieProperty() {
        return lengthMovie;
    }

    public void setLengthMovie(String lengthMovie) {
        this.lengthMovie.set(lengthMovie);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
