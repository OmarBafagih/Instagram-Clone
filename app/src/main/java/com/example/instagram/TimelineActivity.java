package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = "TimelineActivity";
    private BottomNavigationView bottomNavigationView;
    private RecyclerView postsRecyclerView;
    protected PostsAdapter postsAdapter;
    protected List<Post> posts;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //getting reference to recycler view that will be populated by post items
        postsRecyclerView = findViewById(R.id.rvPosts);
        //initializing posts array and adapter
        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(this, posts);
        // set the adapter on the recycler view
        postsRecyclerView.setAdapter(postsAdapter);
        // set the layout manager on the recycler view
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
        queryPosts();


        //getting reference to bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsAdapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_dark);

    }


    public void queryPosts(){
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.USER_KEY);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsFound, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    System.out.println("post");
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                posts.addAll(postsFound);
                postsAdapter.notifyDataSetChanged();
            }
        });

    }
}

