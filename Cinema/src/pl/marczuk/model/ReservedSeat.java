package pl.marczuk.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "reserved_seat")
public class ReservedSeat implements Serializable {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    Reservation reservation;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    Seance seance;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    User seatOwner;

    @DatabaseField
    String seatId;

    public ReservedSeat() {
    }

    public ReservedSeat(Reservation reservation, Seance seance, User seatOwner, String seatId) {
        this.reservation = reservation;
        this.seance = seance;
        this.seatOwner = seatOwner;
        this.seatId = seatId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public Integer getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public User getSeatOwner() {
        return seatOwner;
    }

    public void setSeatOwner(User seatOwner) {
        this.seatOwner = seatOwner;
    }
}
