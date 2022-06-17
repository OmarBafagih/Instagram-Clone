package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.fragments.FeedFragment;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetails extends AppCompatActivity {





    private TextView usernameTextView, descriptionTextView, timePostedTextView, usernameTextViewInDetails;
    private ImageView contentImageView, leaveImageView, profilePhotoImageView;
    private Date datePosted;
    private String datePostedString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        usernameTextView = findViewById(R.id.tvUsernameDetails);
        contentImageView = findViewById(R.id.ivImageDetails);
        descriptionTextView = findViewById(R.id.tvDescriptionDetails);
        timePostedTextView = findViewById(R.id.tvRelativeTimeAgoDetails);
        usernameTextViewInDetails = findViewById(R.id.tvPostUserDetails);
        leaveImageView = findViewById(R.id.ivBack);
        profilePhotoImageView = findViewById(R.id.ivPostUserDetails);

        datePosted = post.getCreatedAt();
        datePostedString = calculateTimeAgo(datePosted);

        //set the values for the views
        usernameTextViewInDetails.setText(post.getUser().getUsername());
        descriptionTextView.setText(post.getDescription());
        usernameTextView.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            int radius = 50;
            Glide.with(this)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(radius))
                    .into(contentImageView);
        }

        ParseFile profilePhoto = ParseUser.getCurrentUser().getParseFile("profilePhoto");
        if (image != null) {
            int radius = 100;
            Glide.with(this)
                    .load(profilePhoto.getUrl())
                    .circleCrop()
                    .into(profilePhotoImageView);
        }

        timePostedTextView.setText(datePostedString + " ago");

        //onClick listener for the leaveImageView
        leaveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






    }
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }



}