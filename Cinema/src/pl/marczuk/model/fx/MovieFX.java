package pl.marczuk.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;


public class MovieFX {
    private SimpleIntegerProperty id;
    private ObjectProperty<ImageView> movieImage;
    private SimpleStringProperty movieName, category, description, length, year;

    public ObjectProperty<ImageView> getMovieImage() {
        return movieImage;
    }
    public MovieFX(Integer id, String movieName, String category, String description, String length, String year, ImageView movieImage) {
        this.id = new SimpleIntegerProperty(id);
        this.movieName = new SimpleStringProperty(movieName);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.length = new SimpleStringProperty(length + " min.");
        this.year = new SimpleStringProperty(year);
        this.movieImage = new SimpleObjectProperty<>(movieImage);
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


    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty movieNameProperty() {
        return movieName;
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }


    public ObjectProperty<ImageView> movieImageProperty() {
        return movieImage;
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLength() {
        return length.get();
    }

    public SimpleStringProperty lengthProperty() {
        return length;
    }

    public void setLength(String length) {
        this.length.set(length);
    }

    public String getYear() {
        return year.get();
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

}
