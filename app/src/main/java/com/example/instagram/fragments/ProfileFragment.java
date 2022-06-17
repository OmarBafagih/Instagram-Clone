package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView profilePictureImageView;
    private TextView profileUsernameTextView;
    private RecyclerView userPostsGridLayout;
    protected PostsAdapter postsAdapter;
    protected List<Post> posts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //getting reference to XML elements
        profilePictureImageView = view.findViewById(R.id.ivUserProfilePhoto);
        profileUsernameTextView = view.findViewById(R.id.tvUserProfileUsername);
        userPostsGridLayout = view.findViewById(R.id.rvUserPosts);



        //initializing posts array and adapter
        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), posts);
        // set the adapter on the recycler view
        userPostsGridLayout.setAdapter(postsAdapter);
        // set the layout manager on the recycler view
        userPostsGridLayout.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // query posts from Parstagram
        queryPosts();



        //set username for the textview that displays the current user's username
        profileUsernameTextView.setText(ParseUser.getCurrentUser().getUsername());





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
