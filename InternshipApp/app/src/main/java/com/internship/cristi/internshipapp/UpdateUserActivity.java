package com.internship.cristi.internshipapp;

import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

public class UpdateUserActivity extends AppCompatActivity {



    //Spinner list
    String[] items = new String[]{"Select a technology", "Android", "iOS", "Java"};
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        UserDetails userDetails = getIntent().getBundleExtra("userDetails").getParcelable("userDetails");

        //Set the spinner
        dropdown = findViewById(R.id.techList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        populateFields(userDetails);
    }

    private void populateFields( UserDetails u) {

        String tech = u.getTeamId();

        ((TextInputEditText) findViewById(R.id.nameText)).setText(u.getName());
        ((TextInputEditText) findViewById(R.id.surnameText)).setText(u.getSuername());
        ((TextInputEditText) findViewById(R.id.mailText)).setText(u.getMail());
        ((TextInputEditText) findViewById(R.id.typeText)).setText(u.getType());
        ((TextInputEditText) findViewById(R.id.aboutText)).setText(u.getAbout());

        dropdown.setSelection(getIndex(dropdown, tech));
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
