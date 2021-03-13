package com.example.polandair.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.example.polandair.room.Favourite;
import com.example.polandair.room.MyDao;
import com.example.polandair.room.MyDatabase;

public class FavouritesRepository {
    private MyDao myDao;


    public FavouritesRepository(Application application) {
        MyDatabase database = MyDatabase.getInstance(application);
        myDao = database.myDao();
    }

    public void insert(Favourite favourite) {
        new InsertFavouriteAsyncTask(myDao).execute(favourite);
    }

    public void update(Favourite favourite) {
        new UpdateFavouriteAsyncTask(myDao).execute(favourite);
    }

    public void delete(Favourite favourite) {
        new DeleteFavouriteAsyncTask(myDao).execute(favourite);
    }



    private static class InsertFavouriteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private MyDao myDao;

        private InsertFavouriteAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourites) {
            myDao.insert(favourites[0]);
            return null;
        }
    }

    private static class UpdateFavouriteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private MyDao myDao;

        private UpdateFavouriteAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourites) {
            myDao.update(favourites[0]);
            return null;
        }
    }

    private static class DeleteFavouriteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private MyDao myDao;

        private DeleteFavouriteAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourites) {
            myDao.delete(favourites[0]);
            return null;
        }
    }

}
