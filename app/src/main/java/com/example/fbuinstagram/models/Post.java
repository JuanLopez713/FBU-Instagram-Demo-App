package com.example.fbuinstagram.models;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String TAG = "Post";
    //description
    public static final String KEY_DESCRIPTION = "description";
    //image
    public static final String KEY_IMAGE = "image";
    //user
    public static final String KEY_USER = "user";
    //KEY_DATE
    public static final String KEY_DATE = "createdAt";
    public static final String KEY_USER_ID = "userId";

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTimeCreatedAt(Date date) {
        String stringDate = String.valueOf(date);
    // String firstPart = stringDate.substring(0, 10);
    // String secondPart = stringDate.substring(24, 28);
        Log.d(TAG, "getTimeCreatedAt: "+ stringDate);
        String twitterFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        Log.d(TAG, "getTimeCreatedAt: " + twitterFormat);
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(stringDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;

       // return firstPart +", "+ secondPart;
    }


}
