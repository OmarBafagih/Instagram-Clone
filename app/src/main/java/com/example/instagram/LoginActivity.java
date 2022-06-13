package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = "LoginActivity";
    private Button btnLogin, btnSignup;
    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //getting references to XML elements
        editTextUsername = (EditText) findViewById(R.id.etUsername);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        //creating on click listeners for login and signup buttons

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login button");
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(username, password);

            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login button");

            }
        });



    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);

    }
}