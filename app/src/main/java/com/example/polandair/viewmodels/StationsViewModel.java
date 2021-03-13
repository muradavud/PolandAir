package com.example.polandair.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.polandair.repositories.StationsRepository;
import com.example.polandair.room.Station;

import java.util.List;

public class StationsViewModel extends AndroidViewModel {
    private StationsRepository repository;
    private LiveData<List<Station>> allStations;
    private LiveData<List<Station>> allFavourites;

    public StationsViewModel(@NonNull Application application) {
        super(application);
        repository = new StationsRepository(application);
        allStations = repository.getAllStations();
        allFavourites = repository.getAllFavourites();
    }

    public LiveData<List<Station>> getAllStations() { return allStations; }


    public void insert(Station station) {
        repository.insert(station);
    }

    public void update(Station station) {
        repository.update(station);
    }

    public void delete(Station station) {
        repository.delete(station);
    }



    public LiveData<List<Station>> getAllFavourites() {
        return allFavourites;
    }

}
