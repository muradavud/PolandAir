package com.example.polandair.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class Favourite {

    @PrimaryKey
    private int stationId;

    //  ... getters and setters

    public Favourite() {
    }

    public Favourite(int stationId) {
        this.stationId = stationId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

}
