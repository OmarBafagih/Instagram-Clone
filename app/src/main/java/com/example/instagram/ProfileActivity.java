package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        //getting reference to log out button and defining its onClick listener
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });



        //getting reference to bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //setting the selected item for the nav bar
        bottomNavigationView.setSelectedItemId(R.id.miProfile);


        //bottom navigation view item listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miHome:
                        startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.miPost:
                        startActivity(new Intent(getApplicationContext(), PostActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.miProfile:
                        return true;
                }


                return false;
            }
        });

    }
}