package com.internship.cristi.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.UserDetails;

import java.util.ArrayList;
import java.util.Observer;


public class TeamFragment extends Fragment {

    FirebaseService firebaseService;


    ListView teamListVew;
    ArrayList<UserDetails> teamDetails;

    public TeamFragment() {
        // Required empty public constructor
    }

    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team, container, false);

        firebaseService = FirebaseService.getInstance();
//        firebaseService.getObservers().add((Observer) this);
        teamDetails = firebaseService.getUsersByTeam(firebaseService.getCurrentUserDetails().getTeamId());
        teamListVew = view.findViewById(R.id.teamListView);

        populateListView();

        return view;
    }

    private void populateListView() {
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                firebaseService.getUsersNameByTeam(firebaseService.getCurrentUserDetails().getTeamId()));

        teamListVew.setAdapter(teamAdapter);
        Log.d("+++Cristi", String.valueOf(teamDetails.size()));
        teamListVew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("userDetails",teamDetails.get((int)l));
                intent.putExtra("userDetails", b);
                startActivity(intent);
            }
        });
    }

}
