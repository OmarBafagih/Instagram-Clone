package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetails extends AppCompatActivity {



    private BottomNavigationView bottomNavigationView;
    private String postDescription, postUsername, timePosted;
    private TextView relativeTimeAgoTextView, descriptionTextView, usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postDescription = getIntent().getStringExtra("description");
        postUsername = getIntent().getStringExtra("user");
        timePosted = getIntent().getStringExtra("relativeTime");


        //getting reference to bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //setting the selected item for the nav bar
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
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
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }

                return false;
            }
        });

        //initializing text views and populating with post details
        usernameTextView = (TextView) findViewById(R.id.tvPostDetailsUser);
        relativeTimeAgoTextView = (TextView) findViewById(R.id.tvPostDetailsRelativeTime);
        descriptionTextView = (TextView) findViewById(R.id.tvPostDetailsDescription);

        usernameTextView.setText("Posted by: " + postUsername);
        descriptionTextView.setText("Caption: " + postDescription);
        relativeTimeAgoTextView.setText("Posted: " + timePosted + " ago");







    }
}