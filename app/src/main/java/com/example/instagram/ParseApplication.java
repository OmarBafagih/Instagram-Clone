package com.example.instagram;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NPdNNSFH6RDu0q4UGUkzwljPPFIb9vN1BpVKcIa9")
                .clientKey("8dxjSZte3Ob0vrTJfFPbAmfZFj2omXP8yWkG1TIs")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
