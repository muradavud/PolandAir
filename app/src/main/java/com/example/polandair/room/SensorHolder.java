package com.example.polandair.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.polandair.model.SensorDataPOJO;

import java.util.List;

@Entity(tableName = "sensorHolder_table")
public class SensorHolder {

    @PrimaryKey
    private int id;
    private int stationId;
    private String code;
    @TypeConverters(ListConverter.class)
    public List<SensorDataPOJO.Value> values;

    //  ... getters and setters

    public List<SensorDataPOJO.Value> getValues() {
        return values;
    }

    public void setValues(List<SensorDataPOJO.Value> values) {
        this.values = values;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }




    public SensorHolder() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }


}
