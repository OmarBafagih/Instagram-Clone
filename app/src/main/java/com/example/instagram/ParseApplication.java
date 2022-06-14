package com.example.instagram;

import android.app.Application;
import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NPdNNSFH6RDu0q4UGUkzwljPPFIb9vN1BpVKcIa9")
                .clientKey("8dxjSZte3Ob0vrTJfFPbAmfZFj2omXP8yWkG1TIs")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
