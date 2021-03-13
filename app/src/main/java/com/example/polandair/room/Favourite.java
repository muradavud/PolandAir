package com.example.polandair.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class Favourite {

    @PrimaryKey
    private int stationId;

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
