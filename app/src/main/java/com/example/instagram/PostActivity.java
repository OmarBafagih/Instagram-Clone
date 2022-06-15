package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PostActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnSubmit, btnTakePicture;
    ImageView imageViewPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //getting reference to xml elements
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imageViewPostImage = (ImageView) findViewById(R.id.imageViewPostImage);

        //setting the selected item for the nav bar
        bottomNavigationView.setSelectedItemId(R.id.miPost);

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


        //onClick listener for TakePicture button
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //onClick listener for Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}