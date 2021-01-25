package com.example.polandair.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "station_table")
public class Station {

    @PrimaryKey
    private int id;
    private String stationName;
    private String gegrLat;
    private String gegrLon;
    private String addressStreet;
    private String index;

    //  ... getters and setters

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Station() {
    }

    @Ignore
    public Station(int id, String stationName, String gegrLat, String gegrLon, String addressStreet, String index) {
        this.id = id;
        this.stationName = stationName;
        this.gegrLat = gegrLat;
        this.gegrLon = gegrLon;
        this.addressStreet = addressStreet;
        this.index = index;
    }

    //@Ignore
   /* private String indexLevelName;

    public String getIndexLevelName() {
        return indexLevelName;
    }

    public void setIndexLevelName(String indexLevelName) {
        this.indexLevelName = indexLevelName;
    }*/

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }

    public String getGegrLat() {
        return gegrLat;
    }

    public String getGegrLon() {
        return gegrLon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setGegrLat(String gegrLat) {
        this.gegrLat = gegrLat;
    }

    public void setGegrLon(String gegrLon) {
        this.gegrLon = gegrLon;
    }
}
