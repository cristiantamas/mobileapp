package com.internship.cristi.internshipapp;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internship.cristi.internshipapp.model.Event;
import com.internship.cristi.internshipapp.model.UserDetails;
import com.internship.cristi.internshipapp.utils.AddEvent;

public class AddEventActivity extends AppCompatActivity {

    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();

    UserDetails currentUserDetails;
    SharedPreferences sharedPreferences;
    Spinner dropdown;

    //Spinner list
    String[] items = new String[]{"Select the team to be notified", "All", "Android", "iOS", "Java"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //Set the spinner
        dropdown = findViewById(R.id.techList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefsFile", 0);

        setCurrentUserDetails();
    }

    public void addEvent(View view) {

        String name     = ((TextInputEditText) findViewById(R.id.eventNameInput)).getText().toString();
        String room     = ((TextInputEditText) findViewById(R.id.roomInput)).getText().toString();
        String datetime = ((TextInputEditText) findViewById(R.id.datetimeInput)).getText().toString();
        String details  = ((TextInputEditText) findViewById(R.id.datailsInput)).getText().toString();
        String owner    = currentUserDetails.getUserId();
        String team     = dropdown.getSelectedItem().toString();

        Event e = new Event(name, datetime, owner, room, details, team);

        AddEvent addEvent = new AddEvent(e);
        if (addEvent.addEvent() == 1) {
            Toast.makeText(getApplicationContext(), "Event added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Could not add event", Toast.LENGTH_LONG).show();
        }
        finish();
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
    }
}
