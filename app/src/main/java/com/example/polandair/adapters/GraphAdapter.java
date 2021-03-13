package com.example.polandair.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polandair.R;
import com.example.polandair.model.SensorDataPOJO;
import com.example.polandair.room.SensorHolder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private ArrayList<SensorHolder> sensorData = new ArrayList<>();

    public GraphAdapter(ArrayList<SensorHolder> sensorHolders) {
        this.sensorData = sensorHolders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_item, parent, false);
        return new GraphAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(sensorData.get(position).getCode());

        List<Entry> entries = new ArrayList<>();
        List<SensorDataPOJO.Value> values = sensorData.get(position).getValues();

        float xx = 0;
        List<String> timestamps = new ArrayList<>();
        if (values.size() < 7) { return; }
        values.subList(7, values.size()).clear();
        Collections.reverse(values);
        for (SensorDataPOJO.Value value : values) {
            if (xx == 7) {
                break;
            }
            float x = xx;
            timestamps.add(value.getDate().substring(11,16));
            xx = xx + 1;

            float y = (float)value.getValue();

            entries.add(new Entry(x,y));


        }
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                //if (timestamps.size() < value) {return "!";}
                return timestamps.get((int) value);
            }
        };

        XAxis xAxis = holder.chart.getXAxis();
        //xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
        //dataSet.setColor(R.color.black);
        //dataSet.setValueTextColor(...); // styling, ...

        LineData lineData = new LineData(dataSet);
        holder.chart.setData(lineData);
        //holder.chart.setMaxVisibleValueCount(5);
        //holder.chart.setVisibleXRangeMaximum(7);
        holder.chart.invalidate(); // refresh
    }

    @Override
    public int getItemCount() {
        return sensorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LineChart chart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewGraph);
            chart = itemView.findViewById(R.id.chart);
        }
    }
}
