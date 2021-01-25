package com.example.polandair.model;

import com.example.polandair.model.StationPOJO;
import com.example.polandair.model.SensorPOJO;
import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.model.StationIndexPOJO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @GET("station/findAll")
    Call<List<StationPOJO>> getStations();

    @GET("aqindex/getIndex/{id}")
    Call<StationIndexPOJO> getStationIndex(@Path("id") int stationId);

    @GET("station/sensors/{id}")
    Call<List<SensorPOJO>> getSensors(@Path("id") int sensorId);

    @GET("data/getData/{id}")
    Call<SensorDataPOJO> getSensorData(@Path("id") int sensorId);
}

