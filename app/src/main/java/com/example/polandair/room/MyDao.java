package com.example.polandair.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.polandair.room.SensorHolder;
import com.example.polandair.room.Station;

import java.util.List;

@Dao
public interface MyDao {

    @Insert
    void insert(Station station);

    @Update
    void update(Station station);

    @Delete
    void delete(Station station);

    @Query("DELETE FROM station_table")
    void deleteAllStations();

    //@Query("SELECT st.id, st.stationName, st.gegrLat, st.gegrLon, si.emb_indexLevelName as indexLevelName FROM station_table st INNER JOIN stationIndex_table si ON st.id = si.id")
    //LiveData<List<StationFull>> getAllStations();

    @Query("SELECT * FROM station_table ORDER BY id ASC")
    LiveData<List<Station>> getAllStations();

    @Query("SELECT st.id, st.stationName, st.addressStreet, st.'index' FROM station_table st INNER JOIN favourite_table ft ON st.id = ft.stationId")
    LiveData<List<Station>> getAllFavourites();

    //@Query("UPDATE station_table SET isFavourite = 1 WHERE id = :id")
    //void updateFavourite(int id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SensorHolder sensorHolder);

    @Update
    void update(SensorHolder sensorHolder);

    @Delete
    void delete(SensorHolder sensorHolder);

    @Query("SELECT * FROM sensorHolder_table ORDER BY id ASC")
    LiveData<List<SensorHolder>> getSensors();

    @Query("DELETE FROM sensorHolder_table")
    void deleteAllSensors();

    @Insert
    void insert(Favourite favourite);

    @Update
    void update(Favourite favourite);

    @Delete
    void delete(Favourite favourite);
}
