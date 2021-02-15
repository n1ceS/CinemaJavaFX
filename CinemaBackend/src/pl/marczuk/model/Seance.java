package pl.marczuk.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DatabaseTable(tableName = "seance")
public class Seance implements Serializable {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Movie movie;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalTime startTime;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate date;

    @ForeignCollectionField
    ForeignCollection<ReservedSeat> reservedSeatList;


    public Seance() {
    }

    public Seance(Movie movie, LocalTime startTime, LocalDate date) {
        this.movie = movie;
        this.startTime = startTime;
        this.date = date;
        this.reservedSeatList = reservedSeatList;
    }


    public Integer getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public ForeignCollection<ReservedSeat> getReservedSeatList() {
        return reservedSeatList;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
