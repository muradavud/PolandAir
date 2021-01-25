package com.example.polandair;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polandair.model.JsonPlaceHolderApi;
import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.model.SensorPOJO;
import com.example.polandair.model.StationIndexPOJO;
import com.example.polandair.room.Favourite;
import com.example.polandair.room.SensorHolder;
import com.example.polandair.room.Station;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapterFavourites extends RecyclerView.Adapter<RecyclerViewAdapterFavourites.ViewHolder> {

    private ArrayList<String> nNames = new ArrayList<>();
    private ArrayList<String> nAddresses = new ArrayList<>();
    private ArrayList<String> nIndeces = new ArrayList<>();
    private List<Station> nStations = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TextView helper2;


    public RecyclerViewAdapterFavourites(Context mContext, RecyclerView recyclerView, ArrayList<String> nNames,
                                         ArrayList<String> nAddresses, ArrayList<String> nIndeces,
                                         List<Station> nStations, TextView helper2) {
        this.nNames = nNames;
        this.nAddresses = nAddresses;
        this.mContext = mContext;
        this.nStations = nStations;
        this.nIndeces = nIndeces;
        this.mRecyclerView = recyclerView;
        this.helper2 = helper2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.station = nStations.get(position);
        holder.stationName.setText(nNames.get(position));
        holder.stationAddress.setText(nAddresses.get(position));
        if (holder.station.getIndex().equals("null")) { holder.stationIndex.setText("Loading..."); }
        else { holder.stationIndex.setText(holder.station.getIndex()); }

        if (nStations.get(position).getIndex() != null){
            switch(nStations.get(position).getIndex()) {
                case "Bardzo zły":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient1)); break;
                case "Zły":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient2)); break;
                case "Dostateczny":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient3)); break;
                case "Umiarkowany":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient4)); break;
                case "Dobry":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient5)); break;
                case "Bardzo dobry":
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient6)); break;
                default:
                    holder.cardGradient.setBackground(mContext.getResources().getDrawable(R.drawable.favourite_card_gradient7)); break;
            }
        }

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(mContext,"Favorite removed!" , Toast.LENGTH_SHORT).show();
            FavouritesRepository favouritesRepository = new FavouritesRepository((Application) mContext.getApplicationContext());
            favouritesRepository.delete(new Favourite(nStations.get(viewHolder.getAdapterPosition()).getId()));

            nNames.remove(viewHolder.getAdapterPosition());
            nAddresses.remove(viewHolder.getAdapterPosition());
            nStations.remove(viewHolder.getAdapterPosition());

            notifyDataSetChanged();
        }

    };

    @Override
    public int getItemCount() {
        return nNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;
        TextView stationAddress;
        TextView stationIndex;
        MaterialButton deleteButton;
        Station station;
        MaterialCardView cardView;
        ConstraintLayout cardGradient;

        public ViewHolder(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.stationName);
            stationAddress = itemView.findViewById(R.id.stationAddress);
            stationIndex = itemView.findViewById(R.id.stationIndex);
            cardView = itemView.findViewById(R.id.fav_cardView);
            cardGradient = itemView.findViewById(R.id.card_gradient);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    helper2.setVisibility(View.GONE);
                    int stationId = nStations.get(getAdapterPosition()).getId();
                    getStationData(stationId);
                }
            });
        }
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
                SensorHolderRepository sensorHolderRepository =
                        new SensorHolderRepository((Application) mContext.getApplicationContext());
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

                SensorHolderRepository sensorHolderRepository = new SensorHolderRepository((Application) mContext.getApplicationContext());
                sensorHolderRepository.insert(sensorHolder);
            }

            @Override
            public void onFailure(Call<SensorDataPOJO> call, Throwable t) {

            }
        });
    }

}
