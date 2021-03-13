package com.example.polandair.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.polandair.room.MyDao;
import com.example.polandair.room.MyDatabase;
import com.example.polandair.room.Station;

import java.util.List;

public class StationsRepository {
    private MyDao myDao;
    private LiveData<List<Station>> allStations;
    private LiveData<List<Station>> allFavourites;

    public StationsRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        myDao = database.myDao();
        allStations = myDao.getAllStations();
        allFavourites = myDao.getAllFavourites();
    }

    public LiveData<List<Station>> getAllStations() {
        return allStations;
    }

    public LiveData<List<Station>> getAllFavourites() {
        return allFavourites;
    }

    public void insert(Station station) {
        new InsertStationAsyncTask(myDao).execute(station);
    }

    public void update(Station station) {
        new UpdateStationAsyncTask(myDao).execute(station);
    }

    public void delete(Station station) {
        new DeleteStationAsyncTask(myDao).execute(station);
    }

    public void deleteAll() {
        new StationsRepository.DeleteAllStationsAsyncTask(myDao).execute();
    }




    //public void updateFavourite(int id) { new UpdateFavouriteAsyncTask(myDao).execute(id); }

    private static class InsertStationAsyncTask extends AsyncTask<Station, Void, Void> {
        private MyDao myDao;

        private InsertStationAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Station... stations) {
            myDao.insert(stations[0]);
            return null;
        }


    }

        private static class UpdateStationAsyncTask extends AsyncTask<Station, Void, Void> {
        private MyDao myDao;

        private UpdateStationAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Station... stations) {
            myDao.update(stations[0]);
            return null;
        }


    }

    private static class DeleteStationAsyncTask extends AsyncTask<Station, Void, Void> {
        private MyDao myDao;

        private DeleteStationAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Station... stations) {
            myDao.delete(stations[0]);
            return null;
        }
    }

    private static class DeleteAllStationsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao myDao;

        private DeleteAllStationsAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllStations();
            return null;
        }
    }

}
