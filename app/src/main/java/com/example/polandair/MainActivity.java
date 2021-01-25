package com.example.polandair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.room.Favourite;
import com.example.polandair.room.SensorHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private SensorHolderViewModel myViewModel;
    private NavController.OnDestinationChangedListener listener;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    public int currentStationId;
    private List<SensorHolder> sensorHolderBundle;
    private NavController navController;
    public static final String SHARED_PREFS = "FavouritesPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem info = menu.findItem(R.id.infoFragment);
        MenuItem favourite = menu.findItem(R.id.favouritesFragment);
        MenuItem map = menu.findItem(R.id.mapsFragment);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.infoFragment) {
                findViewById(R.id.recyclerView).setVisibility(View.GONE);
                findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                info.setEnabled(false);
                map.setEnabled(true);
                favourite.setEnabled(true);

            }
            if (destination.getId() == R.id.favouritesFragment) {
                findViewById(R.id.recyclerView).setVisibility(View.GONE);
                findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
                info.setEnabled(true);
                map.setEnabled(true);
                favourite.setEnabled(false);
            }
            if (destination.getId() == R.id.mapsFragment) {
                findViewById(R.id.recyclerView).setVisibility(View.GONE);
                findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                info.setEnabled(true);
                map.setEnabled(false);
                favourite.setEnabled(true);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Added to favorite!", Toast.LENGTH_SHORT).show();
                Favourite favourite = new Favourite();
                favourite.setStationId(currentStationId);
                FavouritesRepository favouritesRepository = new FavouritesRepository(getApplication());
                favouritesRepository.delete(favourite);
                favouritesRepository.insert(favourite);

                recyclerView.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        SensorHolderRepository sensorHolderRepository = new SensorHolderRepository(getApplication());

        myViewModel = new ViewModelProvider( this)
                .get(SensorHolderViewModel.class);
        myViewModel.getSensors().observe(this, new Observer<List<SensorHolder>>() {
            @Override
            public void onChanged(List<SensorHolder> sensorHolders) {
                if (sensorHolders.size() == 0) {return;}
                    currentStationId = sensorHolders.get(0).getStationId();
                    sensorHolderBundle = sensorHolders;
                    ArrayList<String> sensor = new ArrayList<>();
                    ArrayList<String> number = new ArrayList<>();
                    ArrayList<String> unit = new ArrayList<>();
                    //recyclerView.setAdapter(adapter);
                    Log.d("pls", "onChanged: " + sensorHolders.size());
                    for (SensorHolder sensorHolder : sensorHolders) {
                        //sensorHolderRepository.delete(sensorHolder);
                        sensor.add(sensorHolder.getCode());

                        SensorDataPOJO.Value mostRecent = getMostRecent(sensorHolder);
                        if (mostRecent != null) {
                            number.add(String.valueOf(round(mostRecent.getValue(), 1)));
                        }
                        else {
                            number.add("No data");
                        }
                        unit.add("μg/m3");
                    }
                    RecyclerViewAdapterHorizontal adapter = new RecyclerViewAdapterHorizontal(MainActivity.this, sensor,
                            number, unit, navController, fab, recyclerView);
                    recyclerView.setAdapter(adapter);
                }
        });
    }

    private void addFragment(List<SensorHolder> sensorHolderBundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment, GraphFragment.newInstance(sensorHolderBundle));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void menuItemOnClick(MenuItem menuItem) {
        onBackPressed();
    }

    public SensorDataPOJO.Value getMostRecent(SensorHolder sensorHolder){
        if (sensorHolder.getValues().size() == 0) {
            return null;
        }
        for (SensorDataPOJO.Value value : sensorHolder.getValues()) {
            if (value.getValue() != 0) {
                return value;
            }
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}