package com.example.polandair;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polandair.room.SensorHolder;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterHorizontal extends RecyclerView.Adapter<RecyclerViewAdapterHorizontal.ViewHolder> {

    private ArrayList<String> sensors = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private ArrayList<String> units = new ArrayList<>();
    private Context mContext;
    private NavController navController;

    private FloatingActionButton fab;
    private RecyclerView rv;

    public RecyclerViewAdapterHorizontal(Context mContext, ArrayList<String> sensor, ArrayList<String> number,
                                         ArrayList<String> unit, NavController navController, FloatingActionButton fab, RecyclerView rv) {
        this.sensors = sensor;
        this.numbers = number;
        this.units = unit;
        this.mContext = mContext;
        this.navController = navController;
        this.fab = fab;
        this.rv = rv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sensor.setText(sensors.get(position));
        holder.number.setText(numbers.get(position));
        holder.unit.setText(units.get(position));
        holder.cardView.setCardBackgroundColor(getIndexColor(sensors.get(position), numbers.get(position), mContext));

    }

    private int getIndexColor(String sensor, String num, Context context) {
        Log.d("kek", "getIndexColor: " + num + " " + sensor);
        if (!num.matches("-?\\d+(\\.\\d+)?")) {
            return 0;
        }

        int res = 3;
        float n = Float.parseFloat(num);

        if(sensor.equals("PM10")) {
            if (n <= 20) {
                res = 1; }
            if (20.1 <= n && n <= 50) {
                res = 2; }
            if (50.1 <= n && n <= 80) {
                res = 3; }
            if (80.1 <= n && n <= 110) {
                res = 4; }
            if (110.1 <= n && n <= 150) {
                res = 5; }
            if (150 < n) {
                res = 6; }
        }
        if(sensor.equals("PM2.5")) {
            if (n <= 13) {
                res = 1; }
            if (13.1 <= n && n <= 35) {
                res = 2; }
            if (35.1 <= n && n <= 55) {
                res = 3; }
            if (55.1 <= n && n <= 75) {
                res = 4; }
            if (75.1 <= n && n <= 110) {
                res = 5; }
            if (110 < n) {
                res = 6; }

        }
        if(sensor.equals("03")) {
            if (n <= 70) {
                res = 1; }
            if (70.1 <= n && n <= 120) {
                res = 2; }
            if (120.1 <= n && n <= 150) {
                res = 3; }
            if (150.1 <= n && n <= 180) {
                res = 4; }
            if (180.1 <= n && n <= 240) {
                res = 5; }
            if (240 < n) {
                res = 6; }
    }
        if(sensor.equals("NO2")) {
            if (n <= 40) {
                res = 1; }
            if (40.1 <= n && n <= 100) {
                res = 2; }
            if (100.1 <= n && n <= 150) {
                res = 3; }
            if (150.1 <= n && n <= 200) {
                res = 4; }
            if (200.1 <= n && n <= 400) {
                res = 5; }
            if (400 < n) {
                res = 6; }
        }
        if(sensor.equals("SO2")) {
            if (n <= 50) {
                res = 1; }
            if (50.1 <= n && n <= 100) {
                res = 2; }
            if (100.1 <= n && n <= 200) {
                res = 3; }
            if (200.1 <= n && n <= 350) {
                res = 4; }
            if (350.1 <= n && n <= 500) {
                res = 5; }
            if (500 < n) {
                res = 6; }
        }
        if(sensor.equals("C6H6")) {
            if (n <= 6) {
                res = 1; }
            if (6.1 <= n && n <= 11) {
                res = 2; }
            if (11.1 <= n && n <= 16) {
                res = 3; }
            if (16.1 <= n && n <= 21) {
                res = 4; }
            if (21.1 <= n && n <= 51) {
                res = 5; }
            if (51 < n) {
                res = 6; }
        }
        if(sensor.equals("CO")) {
            n = n / 1000;
            if (n <= 3) {
                res = 1; }
            if (3.1 <= n && n <= 7) {
                res = 2; }
            if (7.1 <= n && n <= 11) {
                res = 3; }
            if (11.1 <= n && n <= 15) {
                res = 4; }
            if (15.1 <= n && n <= 21) {
                res = 5; }
            if (21 < n) {
                res = 6; }
        }
        Log.d("yepe", String.valueOf(res));
        switch (res) {
            case 1:
                res = Color.argb(200, 105, 179, 76); break;
            case 2:
                res = Color.argb(200, 171, 221, 60); break;
            case 3:
                res = Color.argb(200, 250, 183, 51); break;
            case 4:
                res = Color.argb(200, 255, 142, 21); break;
            case 5:
                res = Color.argb(200, 255, 78, 17); break;
            case 6:
                res = Color.argb(200, 255, 13, 13); break;
        }
        Log.d("yepe", String.valueOf(res));
        return res;
    }



    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public void setSensors(List<SensorHolder> sensors) {
        Toast.makeText(mContext, "nice", Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sensor;
        TextView number;
        TextView unit;
        MaterialCardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sensor = itemView.findViewById(R.id.textViewSensor);
            number = itemView.findViewById(R.id.textViewNumber);
            unit = itemView.findViewById(R.id.textViewUnit);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rv.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    navController.navigate(R.id.graphFragment);
                }
            });
        }
    }
}
