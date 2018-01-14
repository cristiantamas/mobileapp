package com.internship.cristi.internshipapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internship.cristi.internshipapp.model.UserDetails;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {


    ListView allUsersListView;
    ArrayList<String> users= new ArrayList<>();


    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();


    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        allUsersListView = view.findViewById(R.id.allUsersListView);

        return view;
    }

    ValueEventListener valueEventListener = firebaseRoot.child("userDetails").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot child : children) {

                UserDetails u = child.getValue(UserDetails.class);
                String s = u.getName() +" " + u.getSuername();
                if( u.getType().compareTo("admin") == 0)
                    s += " (mentor)";
                users.add(s);
            }
            populateListView();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    private void populateListView() {
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                users);

        allUsersListView.setAdapter(userAdapter);
    }

}
