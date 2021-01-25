package com.example.polandair.model;

import androidx.room.Embedded;

import java.util.List;

public class SensorDataPOJO {

    private int id;
    public String key;
    @Embedded
    public List<Value> values;

    public SensorDataPOJO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public static class Value {
        public String date;
        public double value;

        public Value() {
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
