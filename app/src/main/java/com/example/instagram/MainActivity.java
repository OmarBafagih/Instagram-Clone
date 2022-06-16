package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagram.fragments.FeedFragment;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TimelineActivity";
    private BottomNavigationView bottomNavigationView;
    private RecyclerView postsRecyclerView;
    protected PostsAdapter postsAdapter;
    protected List<Post> posts;
    private SwipeRefreshLayout swipeContainer;
    private FeedFragment feedFragment;
    private PostFragment postFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing fragments for use with the bottom navigation bar
        feedFragment = new FeedFragment();
        postFragment = new PostFragment();
        profileFragment = new ProfileFragment();

        //getting reference to bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //bottom navigation view item listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.miHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, feedFragment).commit();
                        return true;
                    case R.id.miPost:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, postFragment).commit();
                        return true;
                    case R.id.miProfile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

        //setting the selected item for the nav bar as defaulting to the "home" page
        bottomNavigationView.setSelectedItemId(R.id.miHome);



    }
}

