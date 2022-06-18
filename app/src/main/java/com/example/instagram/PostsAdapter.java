package com.example.instagram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.example.instagram.fragments.FeedFragment;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private Context context;
    private List<Post> posts;
    private static final String TAG = "PostsAdapter";


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView, descriptionTextView, timePostedTextView, usernameTextViewInDetails, commentsTextView, likeCountTextView;
        private ImageView contentImageView, postDetailsImageView, userProfileImageView, likeImageView, commentsImageView;
        private Date datePosted;
        private String datePostedString;
        private LinearLayout linearLayoutPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvUsername);
            contentImageView = itemView.findViewById(R.id.ivImage);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            postDetailsImageView = itemView.findViewById(R.id.ivPostDetails);
            timePostedTextView = itemView.findViewById(R.id.tvRelativeTimeAgo);
            usernameTextViewInDetails = itemView.findViewById(R.id.tvPostUser);
            linearLayoutPost = itemView.findViewById(R.id.linearLayoutPost);
            userProfileImageView = itemView.findViewById(R.id.ivPostUser);
            likeImageView = itemView.findViewById(R.id.ivLike);
            commentsImageView = itemView.findViewById(R.id.ivComment);
            commentsTextView = itemView.findViewById(R.id.tvComments);
            likeCountTextView = itemView.findViewById(R.id.tvLikeCount);




        }

        public void bind(Post post) {

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            Fragment f = manager.findFragmentById(R.id.fragment_container);
            if(f instanceof FeedFragment){

                datePosted = post.getCreatedAt();
                datePostedString = calculateTimeAgo(datePosted);

                //set the values for the views
                usernameTextViewInDetails.setText(post.getUser().getUsername());
                descriptionTextView.setText(post.getDescription());
                usernameTextView.setText(post.getUser().getUsername());
                ParseFile image = post.getImage();
                if (image != null) {
                    int radius = 50;
                    Glide.with(context)
                            .load(image.getUrl())
                            .transform(new RoundedCorners(radius))
                            .into(contentImageView);
                }
                ParseFile profilePhoto = ParseUser.getCurrentUser().getParseFile("profilePhoto");
                if(profilePhoto != null){
                    int radius = 60;
                    Glide.with(context)
                            .load(profilePhoto.getUrl())
                            .circleCrop()
                            .into(userProfileImageView);
                }

                timePostedTextView.setText(datePostedString + " ago");


                //setting view for lies, whether or not user already likes the post before
                JSONArray likes = post.getLikes();
                likeCountTextView.setText(Integer.toString(likes.length()));
                for(int i = 0; i < likes.length(); i++){
                    try {
                        if(likes.getString(i).equals(ParseUser.getCurrentUser().getObjectId())){
                            likeImageView.setSelected(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                //setting onClick listener for the image, if the user clicks on the image, then we'll take them to the Post details activity
                postDetailsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();

                        Post post = posts.get(position);

                        Intent intent = new Intent(context, PostDetails.class);
                        intent.putExtra("post", Parcels.wrap(post));
                        // intent.putExtra("user", post.getUser().getUsername());
                        //  intent.putExtra("relativeTime", datePostedString);
                        context.startActivity(intent);
                    }
                });

                likeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(likeImageView.isSelected()){
                            likeImageView.setSelected(false);
                            JSONArray likes = post.getLikes();
                            likeCountTextView.setText(Integer.toString(likes.length()-1));
                            for(int i = 0; i < likes.length(); i++){
                                try {
                                    if(likes.getString(i).equals(post.getUser().getObjectId())){
                                        likes.remove(i);

                                        post.put("likes", likes);
                                        post.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if(e != null){
                                                    Log.e(TAG, "Error disliking post", e);
                                                }

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{
                            likeImageView.setSelected(true);
                            likeCountTextView.setText(Integer.toString(likes.length()+1));
                            post.addLikes(ParseUser.getCurrentUser());
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e != null){
                                        Log.e(TAG, "error liking post", e);
                                    }

                                }
                            });
                        }




                    }
                });


            }
            else{
                ParseFile image = post.getImage();
                if (image != null) {
                    int radius = 50;
                    Glide.with(context)
                            .load(image.getUrl())
                            .into(contentImageView);
                }

                //setting onClick listener for the image, if the user clicks on the image, then we'll take them to the Post details activity
                linearLayoutPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();

                        Post post = posts.get(position);

                        Intent intent = new Intent(context, PostDetails.class);
                        intent.putExtra("post", Parcels.wrap(post));
                        // intent.putExtra("user", post.getUser().getUsername());
                        //  intent.putExtra("relativeTime", datePostedString);
                        context.startActivity(intent);
                    }
                });

                postDetailsImageView.setVisibility(View.GONE);
                userProfileImageView.setVisibility(View.GONE);
                likeImageView.setVisibility(View.GONE);
                commentsImageView.setVisibility(View.GONE);
                commentsTextView.setVisibility(View.GONE);
                likeCountTextView.setVisibility(View.GONE);



            }




        }
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