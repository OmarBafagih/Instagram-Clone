package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.EndlessRecyclerViewScrollListener;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    private static final String TAG = "FeedFragment";

    private RecyclerView postsRecyclerView;
    protected PostsAdapter postsAdapter;
    protected List<Post> posts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting reference to recycler view that will be populated by post items
        postsRecyclerView = view.findViewById(R.id.rvPosts);
        //initializing posts array and adapter
        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), posts);
        // set the adapter on the recycler view
        postsRecyclerView.setAdapter(postsAdapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postsRecyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // specify what type of data we want to query - Post.class
                ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
                // include data referred by user key
                query.include(Post.USER_KEY);
                // limit query to latest 20 items
                query.setSkip(totalItemsCount * page + 20);
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
        };
        // Adds the scroll listener to RecyclerView
        postsRecyclerView.addOnScrollListener(scrollListener);

        // query posts from Parstagram
        queryPosts();


        swipeContainer = view.findViewById(R.id.swipeContainer);
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



    public void queryPosts() {
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