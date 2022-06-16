package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = "LoginActivity";
    private Button btnLogin, btnSignup;
    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //if the user was already previously logged in
        if(ParseUser.getCurrentUser() != null){
            goToTimeline();
        }

        //getting references to XML elements
        editTextUsername = (EditText) findViewById(R.id.etUsername);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        //creating on click listeners for login and signup buttons
            //Login button onClick listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login button");

                //collect the inputted text from the input fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "please enter your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(username, password);
                }

            }
        });

            //Login button onClick listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Signup button");

                //collect the inputted text from the input fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Cannot have empty username", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Cannot have empty password", Toast.LENGTH_SHORT).show();
                }
                else{
                    signupUser(username, password);
                }


            }
        });



    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with login: ", e);
                    Toast.makeText(LoginActivity.this, "Login information incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
                //navigate to Timeline activity on successful login
                goToTimeline();
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }



    private void signupUser(String username, String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "error with signup: ", e);
                    Toast.makeText(LoginActivity.this, "Login information incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
                //navigate to Timeline activity on successful sing up
                goToTimeline();
                Toast.makeText(LoginActivity.this, "Successfully created account and signed in", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    public void goToTimeline(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
}