package com.internship.cristi.internshipapp;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.Event;
import com.internship.cristi.internshipapp.model.UserDetails;
import com.internship.cristi.internshipapp.utils.SendNotification;

public class AddEventActivity extends AppCompatActivity {

    FirebaseService firebaseService;


    //Class for notifications
    SendNotification sendNotification = new SendNotification(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        firebaseService = FirebaseService.getInstance();;
    }

    public void addEvent(View view) {

        String name     = ((TextInputEditText) findViewById(R.id.eventNameInput)).getText().toString();
        String room     = ((TextInputEditText) findViewById(R.id.roomInput)).getText().toString();
        String datetime = ((TextInputEditText) findViewById(R.id.datetimeInput)).getText().toString();
        String details  = ((TextInputEditText) findViewById(R.id.datailsInput)).getText().toString();
        String owner    = firebaseService.getCurrentUserDetails().getName() + firebaseService.getCurrentUserDetails().getSuername();

        Event e = new Event(name, datetime, owner, room, details);


        firebaseService.addEvent(e);
        sendNotification.sendNotification(e);
        finish();
    }

}

