package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject{

    public static final String IMAGE_KEY = "image";
    public static final String DESCRIPTION_KEY = "description";
    public static final String USER_KEY = "user";
    public static final String LIKE_KEY = "likes";
    //required empty constructor for parceler to work
    public Post(){}

    public String getDescription(){
        return getString(DESCRIPTION_KEY);
    }
    public void setDescription(String description){
        put(DESCRIPTION_KEY, description);
    }


    public JSONArray getLikes(){return getJSONArray(LIKE_KEY);}
    public void addLikes(ParseUser user) {
        add(LIKE_KEY, user.getObjectId());

    }


    public ParseUser getUser(){
        return getParseUser(USER_KEY);
    }
    public void setUser(ParseUser user){
        put(USER_KEY, user);
    }


    public ParseFile getImage(){
        return getParseFile(IMAGE_KEY);
    }

    public void setImage(ParseFile img){
        put(IMAGE_KEY, img);
    }
}
