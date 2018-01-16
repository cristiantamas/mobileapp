package com.internship.cristi.internshipapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity{

    private FirebaseService firebaseService;


    Button signUpButton;
    Spinner dropdown;
    CircleImageView profilePhoto;


    Integer SELECT_FILE = 0;
    Uri selectedImageUri;

    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();


    //Spinner list
    String[] items = new String[]{"Select a technology", "Android", "iOS", "Java"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);


        firebaseService= FirebaseService.getInstance();

        signUpButton = findViewById(R.id.signupButton);
        profilePhoto = findViewById(R.id.profile_image);


        //Set the spinner
        dropdown = findViewById(R.id.techList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    //User details
                    String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
                    String surname = ((EditText) findViewById(R.id.surnameText)).getText().toString();
                    String mail = ((EditText) findViewById(R.id.emailText)).getText().toString();
                    String about = ((EditText) findViewById(R.id.aboutText)).getText().toString();

                    //User profile
                    String username = ((EditText) findViewById(R.id.usernameText)).getText().toString();
                    String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
                    User newUser = new User(username, password);


                    //Store user to database
                    String userId = firebaseService.addUser(newUser);

                    //Store image to database
                    String urlString = firebaseService.uploadPhoto(profilePhoto, newUser.getUsername());


                    UserDetails newUserDetails = new UserDetails(userId,
                                                                 name,
                                                                 surname,
                                                                 mail,
                                                                 "intern",
                                                                 about,
                                                                 urlString,
                                                                 dropdown.getSelectedItem().toString());


                    //Store user details to database
                   firebaseService.addUserDetails(newUserDetails);
                   finish();
                }
                catch (Exception e){
                    e.printStackTrace();;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                selectedImageUri = data.getData();
                profilePhoto.setImageURI(selectedImageUri);
            }
        }
    }


    public void loadPhotoFromGallery(View view) {

        try {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                }
                break;
        }
    }
}
