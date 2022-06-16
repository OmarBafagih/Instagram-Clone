package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private Context context;
    private List<Post> posts;

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
        private TextView usernameTextView, descriptionTextView, timePostedTextView;
        private ImageView contentImageView, postDetailsImageView;
        private Date datePosted;
        private String datePostedString;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvUsername);
            contentImageView = itemView.findViewById(R.id.ivImage);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            postDetailsImageView = itemView.findViewById(R.id.ivPostDetails);
            timePostedTextView = itemView.findViewById(R.id.tvRelativeTimeAgo);


        }

        public void bind(Post post) {

            datePosted = post.getCreatedAt();
            datePostedString = calculateTimeAgo(datePosted);

            //set the values for the views
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

            timePostedTextView.setText(datePostedString + " ago");

            //setting onClick listener for the image, if the user clicks on the image, then we'll take them to the Post details activity
            postDetailsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetails.class);
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("user", post.getUser().getUsername());
                    intent.putExtra("relativeTime", datePostedString);
                    context.startActivity(intent);
                   // overridePendingTransition(0,0);
                }
            });

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