package com.internship.cristi.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.internship.cristi.internshipapp.model.Team;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

public class LoginActivity extends AppCompatActivity {

    EditText usernameLogin;
    EditText passwordLogin;
    Button loginButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        editor= sharedPreferences.edit();

        usernameLogin = findViewById(R.id.usernameText);
        passwordLogin = findViewById(R.id.passwordText);
        loginButton   = findViewById(R.id.loginButon);
    }

    public void checkUser(View v){

        final String userText = usernameLogin.getText().toString();
        final String passText = passwordLogin.getText().toString();
        
        
        if(userText.compareTo("") == 0 || passText.compareTo("") == 0){
            Toast.makeText(this, R.string.incorrectUsernameOrPassword, Toast.LENGTH_LONG).show();
            return;
        }

        ValueEventListener valueEventListener = firebaseRoot.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    User u = child.getValue(User.class);
                    if (u!=null && u.getUsername().compareTo(userText) == 0 && u.getPassword().compareTo(passText) == 0) {

                        Gson gson = new Gson();
                        String json = gson.toJson(u);
                        editor.putString("user", json);
                        editor.apply();
                        addToPreferences(child.getKey());
                        goToMenu();

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void startSignUpActivity(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMenu(){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
    }


    private void addToPreferences(final String id){
        ValueEventListener valueEventListener = firebaseRoot.child("userDetails").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    UserDetails u = child.getValue(UserDetails.class);
                    if (u!= null && u.getUserId().compareTo(id) == 0) {

                        Gson gson = new Gson();
                        String json = gson.toJson(u);
                        editor.putString("userDetails", json);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
