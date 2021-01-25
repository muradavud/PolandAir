package com.example.polandair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.polandair.model.JsonPlaceHolderApi;
import com.example.polandair.model.StationIndexPOJO;
import com.example.polandair.model.StationPOJO;
import com.example.polandair.room.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private Button button;
    private StationsRepository stationsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        stationsRepository = new StationsRepository(getApplication());
        stationsRepository.deleteAll();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gios.gov.pl/pjp-api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<StationPOJO>> call = jsonPlaceHolderApi.getStations();

        call.enqueue(new Callback<List<StationPOJO>>() {
            // ... handle response ...

            @Override
            public void onResponse(Call<List<StationPOJO>> call, Response<List<StationPOJO>> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                List<StationPOJO> stations = response.body();

                for (StationPOJO station:stations) {
                    String content = "";
                    content += station.getId() + ", ";

                    Station stationHolder = new Station(station.getId(),
                            station.getStationName(),
                            station.getGegrLat(),
                            station.getGegrLon(),
                            station.getAddressStreet(),
                            "null");
                            //stationsRepository.delete(stationHolder);
                            stationsRepository.insert(stationHolder);
                            Log.d("test", stationHolder.getId() + ": " + stationHolder.getIndex());

                }
                enqueueIndexRequest(jsonPlaceHolderApi, stations);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<List<StationPOJO>> call, Throwable t) {
            }
        });

    }

   public void enqueueIndexRequest(JsonPlaceHolderApi jsonPlaceHolderApi, List<StationPOJO> stations) {
        for (StationPOJO station : stations) {
            Call<StationIndexPOJO> call = jsonPlaceHolderApi.getStationIndex(station.getId());
            call.enqueue(new Callback<StationIndexPOJO>() {
                @Override
                public void onResponse(Call<StationIndexPOJO> call, Response<StationIndexPOJO> response) {
                    if (!response.isSuccessful()) {
                        textView.append("Code:" + response.code());
                        return;
                    }

                    StationIndexPOJO stationIndex = response.body();
                    String stIndex;
                    if (stationIndex.getStIndexLevel() == null) { stIndex = "Brak!"; }
                    else { stIndex = stationIndex.getStIndexLevel().getIndexLevelName(); }

                    Station stationHolder = new Station(station.getId(),
                            station.getStationName(),
                            station.getGegrLat(),
                            station.getGegrLon(),
                            station.getAddressStreet(),
                            stIndex);
                    stationsRepository.update(stationHolder);
                    Log.d("test", stationHolder.getId() + ": " + stationHolder.getIndex());
                }

                @Override
                public void onFailure(Call<StationIndexPOJO> call, Throwable t) {
                    textView.append(t.getMessage());
                }
            });
        }
    }

}