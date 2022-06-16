package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.instagram.R;


public class PostDetailsFragment extends Fragment {

    private String postDescription, postUsername, timePosted;
    private TextView relativeTimeAgoTextView, descriptionTextView, usernameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initializing text views and populating with post details
        usernameTextView = (TextView) view.findViewById(R.id.tvPostDetailsUser);
        relativeTimeAgoTextView = (TextView) view.findViewById(R.id.tvPostDetailsRelativeTime);
        descriptionTextView = (TextView) view.findViewById(R.id.tvPostDetailsDescription);

        usernameTextView.setText("Posted by: " + postUsername);
        descriptionTextView.setText("Caption: " + postDescription);
        relativeTimeAgoTextView.setText("Posted: " + timePosted + " ago");


    }
}