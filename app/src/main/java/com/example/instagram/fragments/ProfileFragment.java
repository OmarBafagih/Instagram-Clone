package com.example.instagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int RESULT_OK = -1;

    private ImageView profilePictureImageView;
    private TextView profileUsernameTextView;
    private RecyclerView userPostsGridLayout;
    private Button btnAddPhoto;
    protected PostsAdapter postsAdapter;
    protected List<Post> posts;

    File photoFile;
    String photoFileName = "profilePhoto.jpg";



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
        btnAddPhoto = view.findViewById(R.id.btnAddProfilePhoto);


        //initializing posts array and adapter
        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), posts);
        // set the adapter on the recycler view
        userPostsGridLayout.setAdapter(postsAdapter);
        // set the layout manager on the recycler view
        userPostsGridLayout.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;

        // query posts from Parstagram
        queryPosts();



        //set username for the textview that displays the current user's username
        profileUsernameTextView.setText(ParseUser.getCurrentUser().getUsername());

        //set the profile image if it exists
        ParseFile image = ParseUser.getCurrentUser().getParseFile("profilePhoto");
        if (image != null) {
            int radius = 100;
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(profilePictureImageView);
        }

        //setting on click listener for Add Profile Photo button
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("profilePhoto", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Your profile photo has been updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




    }



    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            //cameraLauncher.launch(intent);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                profilePictureImageView = (ImageView) getActivity().findViewById(R.id.ivUserProfilePhoto);
                profilePictureImageView.setImageBitmap(takenImage);
                Log.i(TAG, "image set");
               // btnSubmit.setVisibility(View.VISIBLE);

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        //
        query.include(Post.USER_KEY);
        // limit query to latest 20 items
        query.setLimit(20);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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
