package com.internship.cristi.internshipapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserActivity extends AppCompatActivity {

    FirebaseService firebaseService;
    UserDetails userDetails;

    CircleImageView profilePhoto;


    //Spinner list
    String[] items = new String[]{"Select a technology", "Android", "iOS", "Java"};
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        userDetails = getIntent().getBundleExtra("userDetails").getParcelable("userDetails");
        firebaseService = FirebaseService.getInstance();

        //Set the spinner
        dropdown = findViewById(R.id.techList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setEnabled(false);


        findViewById(R.id.nameText).setEnabled(false);
        findViewById(R.id.surnameText).setEnabled(false);
        findViewById(R.id.mailText).setEnabled(false);
        findViewById(R.id.aboutText).setEnabled(false);
        findViewById(R.id.typeText).setEnabled(false);

        populateFields(userDetails);
        displayButtons((LinearLayout) findViewById(R.id.updateUserLayout));


    }

    private Button createButton(LinearLayout layout, String text){
        Button btn = new Button(this);

        if(text.compareTo("Delete account") == 0){
            btn.setBackgroundColor(Color.RED);
        }

        btn.setText(text);
        btn.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        btn.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.addView(btn);
        return btn;
    }

    private void updateUser(){
        String userid = firebaseService.getCurrentUserDetails().getUserId();
        String name = ((TextInputEditText)findViewById(R.id.nameText)).getText().toString();
        String surname = ((TextInputEditText)findViewById(R.id.surnameText)).getText().toString();
        String mail = ((TextInputEditText)findViewById(R.id.mailText)).getText().toString();
        String type = firebaseService.getCurrentUserDetails().getType();
        String about = ((TextInputEditText)findViewById(R.id.aboutText)).getText().toString();
        String teamId = firebaseService.getCurrentUserDetails().getTeamId();
        String imgPath = firebaseService.getCurrentUserDetails().getImagePath();

        UserDetails ud = new UserDetails(userid, name, surname, mail, type, about, imgPath, teamId);
        ud.setId(userDetails.getId());

        firebaseService.updateUserDetails(ud);
        finish();

    }

    private void displayButtons(LinearLayout layout) {
        String currentId = firebaseService.getCurrentUser().getId();
        String userId = userDetails.getUserId();
        if(currentId.compareTo(userId) ==0)
        {
            findViewById(R.id.nameText).setEnabled(true);
            findViewById(R.id.surnameText).setEnabled(true);
            findViewById(R.id.mailText).setEnabled(true);
            findViewById(R.id.aboutText).setEnabled(true);
            createButton(layout, "Save").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateUser();
                }
            });
        }

        if(currentId.compareTo(userId)!=0) {
            createButton(layout, "Send emal").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* Create the Intent */
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    /* Fill it with Data */
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

                    /* Send it off to the Activity-Chooser */
                    getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });
        }

        if(firebaseService.getCurrentUserDetails().getType().compareTo("admin")==0) {
            createButton(layout, "Delete account").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseService.deleteUser(userDetails.getUserId(), userDetails.getId());
                    finish();
                }
            });

        }




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

    public void loadPhotoFromGallery(View view) {
    }
}
