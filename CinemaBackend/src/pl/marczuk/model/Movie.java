package pl.marczuk.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "movie")
public class Movie implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField()
    private String description;

    @DatabaseField()
    private Integer year;

    @DatabaseField()
    private Integer length;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private byte[] image;

    @DatabaseField()
    private String category;

    public Movie() {
    }

    public Movie(String title, String description, Integer year, Integer length, byte[] image, String category) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.length = length;
        this.image = image;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getLength() {
        return length;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
