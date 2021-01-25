package com.example.polandair;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.polandair.room.SensorHolder;

import java.util.List;

public class SensorHolderViewModel extends AndroidViewModel {
    private SensorHolderRepository repository;
    private LiveData<List<SensorHolder>> allSensors;

    public SensorHolderViewModel(@NonNull Application application) {
        super(application);
        repository = new SensorHolderRepository(application);
        allSensors = repository.getSensors();
    }

    public void insert(SensorHolder sensorHolder) {
        repository.insert(sensorHolder);
    }

    public void update(SensorHolder sensorHolder) {
        repository.update(sensorHolder);
    }

    public void delete(SensorHolder sensorHolder) {
        repository.delete(sensorHolder);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<SensorHolder>> getSensors() {
        return allSensors;
    }

}
