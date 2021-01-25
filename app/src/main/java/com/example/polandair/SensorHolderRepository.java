package com.example.polandair;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.polandair.room.MyDao;
import com.example.polandair.room.MyDatabase;
import com.example.polandair.room.SensorHolder;

import java.util.List;

public class SensorHolderRepository {
    private MyDao myDao;
    private LiveData<List<SensorHolder>> Sensors;

    public SensorHolderRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        myDao = database.myDao();
        Sensors = myDao.getSensors();
    }

    public void insert(SensorHolder sensorHolder) {
        new InsertSensorAsyncTask(myDao).execute(sensorHolder);
    }

    public void update(SensorHolder sensorHolder) {
        new UpdateSensorAsyncTask(myDao).execute(sensorHolder);
    }

    public void delete(SensorHolder sensorHolder) {
        new DeleteSensorAsyncTask(myDao).execute(sensorHolder);
    }

    public void deleteAll() {
        new DeleteAllSensorAsyncTask(myDao).execute();
    }

    public LiveData<List<SensorHolder>> getSensors() {
        return Sensors;
    }

    private static class InsertSensorAsyncTask extends AsyncTask<SensorHolder, Void, Void> {
        private MyDao myDao;

        private InsertSensorAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(SensorHolder... sensorHolders) {
            myDao.insert(sensorHolders[0]);
            return null;
        }


    }

    private static class UpdateSensorAsyncTask extends AsyncTask<SensorHolder, Void, Void> {
        private MyDao myDao;

        private UpdateSensorAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(SensorHolder... sensorHolders) {
            myDao.update(sensorHolders[0]);
            return null;
        }


    }

    private static class DeleteSensorAsyncTask extends AsyncTask<SensorHolder, Void, Void> {
        private MyDao myDao;

        private DeleteSensorAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(SensorHolder... sensorHolders) {
            myDao.delete(sensorHolders[0]);
            return null;
        }
    }

    private static class DeleteAllSensorAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyDao myDao;

        private DeleteAllSensorAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDao.deleteAllSensors();
            return null;
        }
    }

}
