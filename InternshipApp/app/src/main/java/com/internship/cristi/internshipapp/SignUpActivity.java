package com.internship.cristi.internshipapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;
import com.internship.cristi.internshipapp.model.Team;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;
import com.internship.cristi.internshipapp.utils.SendEmail;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity{

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

        Intent intent = getIntent();

        firebaseRoot = FirebaseDatabase.getInstance().getReference();

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


                    DatabaseReference databaseReference = firebaseRoot.child("user").push();
                    String userId = databaseReference.getKey();
                    databaseReference.setValue(newUser);


                    //Store image to database
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference().child(username + ".png");
                    profilePhoto.setDrawingCacheEnabled(true);
                    profilePhoto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    profilePhoto.layout(0, 0, profilePhoto.getMeasuredWidth(), profilePhoto.getMeasuredHeight());
                    profilePhoto.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(profilePhoto.getDrawingCache());

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });


                    UserDetails newUserDetails = new UserDetails(userId,
                                                                 name,
                                                                 surname,
                                                                 mail,
                                                                 "intern",
                                                                 about,
                                                                 storageReference.child(username + ".png").getDownloadUrl().toString(),
                                                                 dropdown.getSelectedItem().toString());
                    firebaseRoot.child("userDetails").push().setValue(newUserDetails);


                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
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
