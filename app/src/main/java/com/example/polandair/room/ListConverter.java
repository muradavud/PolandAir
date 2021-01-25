package com.example.polandair.room;

import androidx.room.TypeConverter;

import com.example.polandair.model.SensorDataPOJO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public String fromValueList(List<SensorDataPOJO.Value> values) {
        if (values == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SensorDataPOJO.Value>>() {}.getType();
        String json = gson.toJson(values, type);
        return json;
    }

    @TypeConverter
    public List<SensorDataPOJO.Value> toValueList(String valueString) {
        if (valueString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SensorDataPOJO.Value>>() {}.getType();
        List<SensorDataPOJO.Value> valueList = gson.fromJson(valueString, type);
        return valueList;
    }
}
