package com.example.instagram;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class PostActivity extends AppCompatActivity {


    private static final String TAG = "PostActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private BottomNavigationView bottomNavigationView;
    private Button btnSubmit, btnTakePicture;
    ImageView imageViewPostImage;
    private EditText editTextPostDescription;

    File photoFile;
    String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //getting reference to xml elements
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        //   imageViewPostImage = (ImageView) findViewById(R.id.imageViewPostImage);
        editTextPostDescription = (EditText) findViewById(R.id.editTextDescription);

        //setting the selected item for the nav bar
        bottomNavigationView.setSelectedItemId(R.id.miPost);

        //bottom navigation view item listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miHome:
                        startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.miPost:
                        return true;
                    case R.id.miProfile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
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
                launchCamera();

            }
        });


        //onClick listener for Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editTextPostDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(PostActivity.this, "Please enter a description for your post", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || imageViewPostImage.getDrawable() == null) {
                    Toast.makeText(PostActivity.this, "Error loading photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });


    }

    public void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));


        //saving the post to the database
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error: could not save post in background", e);
                    Toast.makeText(PostActivity.this, "Sorry, there was an error saving your post, try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Post was saved to database successfully");
                Toast.makeText(PostActivity.this, "Your post is up now!", Toast.LENGTH_SHORT).show();
                editTextPostDescription.setText("");
                imageViewPostImage.setImageResource(0);
            }
        });

    }


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);

    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            //cameraLauncher.launch(intent);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                imageViewPostImage = (ImageView) findViewById(R.id.imageViewPostImage);
                imageViewPostImage.setImageBitmap(takenImage);
                Log.i(TAG, "image set");

            } else { // Result was a failure
                Toast.makeText(PostActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}