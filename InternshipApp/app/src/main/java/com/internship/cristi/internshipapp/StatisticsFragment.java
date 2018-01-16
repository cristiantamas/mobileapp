package com.internship.cristi.internshipapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.internship.cristi.internshipapp.api.FirebaseService;

import java.nio.channels.FileLock;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class StatisticsFragment extends Fragment {


    FirebaseService firebaseService;

    PieChart pieChart;


    ArrayList<Float> yData;
    ArrayList<String> xData;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseService=FirebaseService.getInstance();

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        pieChart = view.findViewById(R.id.techPieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(10);

        addDataSet();

        return view;
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        yData = firebaseService.getStats();
        xData = firebaseService.getTeamNames();

        for(int i = 0; i < yData.size(); i++){
            if(yData.get(i) != 0) {
                float x = Float.parseFloat(String.format("%.2f", yData.get(i)));
                yEntrys.add(new PieEntry(x, xData.get(i) + " %", i));
            }
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Technology proportion");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}
