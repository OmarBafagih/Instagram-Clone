package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    Button btnPost;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //getting reference to bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        //getting reference to the Post button and defining its onClick listener
        btnPost = (Button) findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigating to the Post activity where user can submit a post
                Intent i = new Intent(TimelineActivity.this, PostActivity.class);
                startActivity(i);
            }
        });

        //setting the selected item for the nav bar
        bottomNavigationView.setSelectedItemId(R.id.miHome);

        //bottom navigation view item listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miHome:
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

        //bottom navigation item reselected listener


    }


    private void checkifActivityCurrent(String className){
        //Using the activity manager to check what the current active activity is
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if(taskList.get(0).numActivities == 1 || taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Log.i(TAG, "This is last activity in the stack");
            //do nothing, maybe add a stretch feature to refresh the timeline
        }
        else{
            Intent i = new Intent(TimelineActivity.this, PostActivity.class);
            startActivity(i);
            finish(); //to keep the running activity stack small
        }
    }
}

