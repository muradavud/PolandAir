package com.example.polandair;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polandair.room.SensorHolder;
import com.example.polandair.room.Station;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    private static List<SensorHolder> sensorData;


    public static Fragment newInstance(List<SensorHolder> sensorHolderBundle) {
        GraphFragment fragment = new GraphFragment();
        sensorData = sensorHolderBundle;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvGraphs = view.findViewById(R.id.rvGraphs);
        rvGraphs.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        SensorHolderViewModel model = new ViewModelProvider(requireActivity()).get(SensorHolderViewModel.class);
        model.getSensors().observe(getViewLifecycleOwner(), sensorHolders -> {
            if (sensorHolders.size() == 0) {return;}
            ArrayList<SensorHolder> sensors = new ArrayList<>(sensorHolders);
            GraphAdapter adapter = new GraphAdapter(sensors);
            rvGraphs.setAdapter(adapter);
            Log.d("vvv", "onViewCreated: " + adapter.getItemCount());

            Log.d("heyf", String.valueOf(sensorHolders.get(0).getStationId()));
        });
    }

}