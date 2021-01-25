package com.example.polandair.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Station.class, SensorHolder.class, Favourite.class}, version = 13)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public abstract MyDao myDao();

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class, "my_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
