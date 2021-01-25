package com.example.polandair;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polandair.model.JsonPlaceHolderApi;
import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.model.SensorPOJO;
import com.example.polandair.model.StationIndexPOJO;
import com.example.polandair.room.SensorHolder;
import com.example.polandair.room.Station;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StationsViewModel FavouritesViewModel;
    private RecyclerView HRecyclerView;
    private TextView helper2;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FavouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(StationsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HRecyclerView = getActivity().findViewById(R.id.recyclerView);
        return inflater.inflate(R.layout.fragment_favourites, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper2 = getActivity().findViewById(R.id.helper2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView_favourites);
        recyclerView.setLayoutManager(layoutManager);

        FavouritesViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(StationsViewModel.class);
        FavouritesViewModel.getAllFavourites().observe(getActivity(), stations -> {
            Log.d("haha2", "onViewCreated: updated");
            ArrayList<String> stationName = new ArrayList<>();
            ArrayList<String> stationAddress = new ArrayList<>();
            ArrayList<String> stationIndex = new ArrayList<>();
            for (Station station : stations) {
                stationName.add(station.getStationName());
                stationAddress.add(station.getAddressStreet());
                stationIndex.add(station.getIndex());
                Log.d("ppp", station.getStationName() + "/n" + station.getIndex()  + "/n");
            }
            RecyclerViewAdapterFavourites adapter = new RecyclerViewAdapterFavourites(getActivity(), HRecyclerView, stationName,
                    stationAddress, stationIndex, stations, helper2);
            new ItemTouchHelper(adapter.itemTouchHelperCallback).attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
        });
    }


    public void getStationData(int stationId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gios.gov.pl/pjp-api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<SensorPOJO>> call = jsonPlaceHolderApi.getSensors(stationId);
        call.enqueue(new Callback<List<SensorPOJO>>() {
            @Override
            public void onResponse(Call<List<SensorPOJO>> call, Response<List<SensorPOJO>> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());
                    return;
                }
                List<SensorPOJO> sensorPOJOS = response.body();
                SensorHolderRepository sensorHolderRepository = new SensorHolderRepository(getActivity().getApplication());
                sensorHolderRepository.deleteAll();

                for (SensorPOJO sensorPOJO : sensorPOJOS) {
                    displayStationData(jsonPlaceHolderApi, sensorPOJO);
                }
            }

            @Override
            public void onFailure(Call<List<SensorPOJO>> call, Throwable t) {

            }
        });

        Call<StationIndexPOJO> call2 = jsonPlaceHolderApi.getStationIndex(stationId);
        call2.enqueue(new Callback<StationIndexPOJO>() {
            @Override
            public void onResponse(Call<StationIndexPOJO> call, Response<StationIndexPOJO> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());
                    return;
                }

            }

            @Override
            public void onFailure(Call<StationIndexPOJO> call, Throwable t) {

            }
        });

    }

    public void displayStationData(JsonPlaceHolderApi jsonPlaceHolderApi, SensorPOJO sensorPOJO) {
        Call<SensorDataPOJO> call = jsonPlaceHolderApi.getSensorData(sensorPOJO.getId());
        call.enqueue(new Callback<SensorDataPOJO>() {
            @Override
            public void onResponse(Call<SensorDataPOJO> call, Response<SensorDataPOJO> response) {
                if(!response.isSuccessful()) {
                    return;
                }


                SensorDataPOJO sensorData = response.body();
                Log.d("enq", "onResponse: " + sensorData.getKey());
                SensorHolder sensorHolder = new SensorHolder();
                sensorHolder.setId(sensorPOJO.getId());
                sensorHolder.setCode(sensorPOJO.getParam().getParamCode());
                sensorHolder.setStationId(sensorPOJO.getStationId());
                sensorHolder.setValues(sensorData.getValues());

                SensorHolderRepository sensorHolderRepository = new SensorHolderRepository(getActivity().getApplication());
                sensorHolderRepository.insert(sensorHolder);
            }

            @Override
            public void onFailure(Call<SensorDataPOJO> call, Throwable t) {

            }
        });
    }
}