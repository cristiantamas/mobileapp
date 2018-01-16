package com.internship.cristi.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.Team;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

public class LoginActivity extends AppCompatActivity {


    FirebaseService firebaseService;

    TextInputEditText usernameLogin;
    TextInputEditText passwordLogin;
    Button loginButton;

    SharedPreferences.Editor editor;



    //For firebase
    DatabaseReference firebaseRoot = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseService = FirebaseService.getInstance();

        usernameLogin = findViewById(R.id.usernameText);
        passwordLogin = findViewById(R.id.passwordText);

        usernameLogin.setText("cristiantamas");
        passwordLogin.setText("parola");

        loginButton   = findViewById(R.id.loginButon);
    }

    public void checkUser(View v){

        String userText = usernameLogin.getText().toString();
        String passText = passwordLogin.getText().toString();
        
        
        if(userText.compareTo("") == 0 || passText.compareTo("") == 0){
            Toast.makeText(this, R.string.incorrectUsernameOrPassword, Toast.LENGTH_LONG).show();
            return;
        }
        try {

            User u = firebaseService.getUserByUserName(userText);
            firebaseService.setCurrentUser(u);
            firebaseService.setCurrentUserDetails(firebaseService.getDetailsByUser(u.getId()));
            goToMenu();
        }
        catch (Exception e){
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void startSignUpActivity(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void goToMenu(){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
    }

}
