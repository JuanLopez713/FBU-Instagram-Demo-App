package com.example.fbuinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    //description
    public static final String KEY_DESCRIPTION = "description";
    //image
    public static final String KEY_IMAGE = "image";
    //user
    public static final String KEY_USER = "user";
    //user
    public static final String KEY_DATE = "createdAt";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public String getTimeCreatedAt(Date date) {
        String stringDate = String.valueOf(date);
        String firstPart = stringDate.substring(0, 10);
        String secondPart = stringDate.substring(24, 28);

        return firstPart +", "+ secondPart;
    }


}
