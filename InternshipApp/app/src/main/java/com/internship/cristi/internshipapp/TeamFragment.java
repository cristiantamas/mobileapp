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
import com.internship.cristi.internshipapp.model.UserDetails;

import java.util.ArrayList;


public class TeamFragment extends Fragment {

    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();

    ListView teamListVew;


    ArrayList<UserDetails> teamDetails = new ArrayList<>();
    ArrayList<String> team= new ArrayList<>();
    UserDetails currentUserDetails;
    SharedPreferences sharedPreferences;


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

        teamListVew = view.findViewById(R.id.teamListView);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", 0);


        setCurrentUserDetails();
        populateListView();

        return view;
    }

    private void setCurrentUserDetails() {
        final String value = sharedPreferences.getString("userId", "nu merge");
        ValueEventListener valueEventListener = firebaseRoot.child("userDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    UserDetails u = child.getValue(UserDetails.class);
                    if (u.getUserId().compareTo(value) == 0) {
                        currentUserDetails = u;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        valueEventListener = firebaseRoot.child("userDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {

                    UserDetails u = child.getValue(UserDetails.class);
                    if (u.getTeamId().compareTo(currentUserDetails.getTeamId()) == 0) {
                        String s = u.getName() +" " + u.getSuername();
                        if( u.getType().compareTo("admin") == 0)
                            s += " (mentor)";
                        team.add(s);
                        teamDetails.add(u);
                    }
                }
                populateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateListView() {
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                team);

        teamListVew.setAdapter(teamAdapter);

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
