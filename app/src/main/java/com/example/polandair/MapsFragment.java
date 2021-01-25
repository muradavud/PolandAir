package com.example.polandair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.polandair.model.JsonPlaceHolderApi;
import com.example.polandair.model.SensorPOJO;
import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.room.SensorHolder;
import com.example.polandair.room.Station;
import com.example.polandair.model.StationIndexPOJO;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 12.0f;
    private StationsViewModel myViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private HashMap<Integer, Marker> markerHashMap = new HashMap<Integer, Marker>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private CameraPosition lastCameraPosition;
    private GoogleMap map;
    private boolean locationPermissionGranted;

    static final int color1 = Color.argb(150, 255, 13, 13);
    static final int color2 = Color.argb(150, 255, 78, 17);
    static final int color3 = Color.argb(150, 255, 142, 21);
    static final int color4 = Color.argb(150, 250, 183, 51);
    static final int color5 = Color.argb(150, 171, 221, 60);
    static final int color6 = Color.argb(150, 105, 179, 76);
    static final int color7 = Color.argb(150, 189, 189, 189);


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            getLocationPermission();
            updateLocationUI();
            getDeviceLocation();

            myViewModel.getAllStations().observe(getActivity(), stations -> {
                // Update UI...


                for (Station station : stations) {
                    if (markerHashMap.containsKey(station.getId())) {
                        Log.d("opp", "onMapReady: df");
                        if (!station.getIndex().equals("null")) {
                            Marker marker = markerHashMap.get(station.getId());
                            marker.setTag(station);
                            switch(station.getIndex()) {
                                case "Bardzo zły":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)); break;
                                case "Zły":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)); break;
                                case "Dostateczny":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker3)); break;
                                case "Umiarkowany":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker4)); break;
                                case "Dobry":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker5)); break;
                                case "Bardzo dobry":
                                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker6)); break;
                                default:
                            }
                        }
                    }
                    else {
                        double latitude = Double.parseDouble(station.getGegrLat());
                        double longitude = Double.parseDouble(station.getGegrLon());
                        LatLng latLng = new LatLng(latitude, longitude);
                        String address = station.getAddressStreet();
                        if (address == null) { address = "bez ulicy"; }
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(address)
                                .anchor(0.5f, 0.5f));
                        marker.setTag(station);

                        switch(station.getIndex()) {
                            case "Bardzo zły":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker1)); break;
                            case "Zły":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker2)); break;
                            case "Dostateczny":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker3)); break;
                            case "Umiarkowany":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker4)); break;
                            case "Dobry":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker5)); break;
                            case "Bardzo dobry":
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker6)); break;
                            default:
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker7));
                        }

                        markerHashMap.put(station.getId(), marker);
                    }

                }

            });
            LatLngBounds polandBounds = new LatLngBounds(
                    new LatLng(49.021882, 14.687256), // SW bounds
                    new LatLng(54.828143, 27.638993)  // NE bounds
            );
            // Constrain the camera target to the Adelaide bounds.
            googleMap.setLatLngBoundsForCameraTarget(polandBounds);
            googleMap.setMinZoomPreference(5.5f);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);


            LatLng center;
            if (lastCameraPosition != null) {
                center = new LatLng(lastCameraPosition.target.latitude, lastCameraPosition.target.longitude);
            }
            else {
                center = new LatLng(51.9194, 19.1451);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(center));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    recyclerView.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);

                    Station station = (Station) marker.getTag();
                    getStationData(station.getId());

                    if (station.getIndex().equals("null")) { marker.setSnippet("Loading..."); }
                    else { marker.setSnippet(station.getIndex()); }
                    marker.showInfoWindow();

                    return false;
                }
            });

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    floatingActionButton.setVisibility(View.INVISIBLE);

                }
            });
        }
    };


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            Log.d("sukaa", "getLocationPermission: net1");
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.d("sukaa", "getLocationPermission: net2");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        Log.d("yopta", "onRequestPermissionsResult: ");
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            Log.d("sukaa", "updateLocationUI: da");
            return;
        }
        try {
            if (locationPermissionGranted) {
                Log.d("sukaa", "updateLocationUI: da1");
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                Log.d("sukaa", "updateLocationUI: da2");
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                //getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = ((MainActivity)getActivity()).findViewById(R.id.recyclerView);
        floatingActionButton = ((MainActivity)getActivity()).findViewById(R.id.floatingActionButton);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        myViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(StationsViewModel.class);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lastCameraPosition = map.getCameraPosition();
        Log.d("heyaaa", "onDestroyView: ");

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("heyaaa", "onDestroy: ");

    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

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