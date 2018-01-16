package com.internship.cristi.internshipapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.internship.cristi.internshipapp.api.FirebaseService;



/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {


    FirebaseService firebaseService;

    ListView allEventsListView;



    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseService = FirebaseService.getInstance();

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        allEventsListView = view.findViewById(R.id.allEventsListView);


        populateListView();

        return view;
    }


    private void populateListView() {
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                firebaseService.getAllEvents());

        allEventsListView.setAdapter(userAdapter);
    }
}
