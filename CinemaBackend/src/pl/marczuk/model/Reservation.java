package pl.marczuk.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "reservation")
public class Reservation implements Serializable {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private User user;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Seance seance;

    @ForeignCollectionField(eager = true)
    ForeignCollection<ReservedSeat> reservedSeatList;

    @DatabaseField
    public String status;
    @DatabaseField
    public String hashCode;
    public Reservation() {
    }

    public Reservation(User user, Seance seance) {
        this.user = user;
        this.seance = seance;
        this.status = "Zarezerwowane";
        this.hashCode = "ABC1234";
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Seance getSeance() {
        return seance;
    }

    public ForeignCollection<ReservedSeat> getReservedSeatList() {
        return reservedSeatList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public void setReservedSeatList(ForeignCollection<ReservedSeat> reservedSeatList) {
        this.reservedSeatList = reservedSeatList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
