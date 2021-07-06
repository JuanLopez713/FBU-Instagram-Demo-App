package com.example.fbuinstagram.models;

import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.fbuinstagram.R;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public static final String LIKED_BY = "likedBy";
    public static final String LIKE_COUNT = "likeCount";

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

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTimeCreatedAt(Date date) {
        String stringDate = String.valueOf(date);

        // Log.d(TAG, "getTimeCreatedAt: " + stringDate);
        String twitterFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        //Log.d(TAG, "getTimeCreatedAt: " + twitterFormat);
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
    }


    public void likePostHandler(ParseUser user) throws JSONException {

        JSONArray likedBy = getJSONArray(LIKED_BY);
        List<String> likedByList = new ArrayList<>();
        String userId = user.getObjectId();
        int likeCount = likedBy.length();
        for (int i = 0; i < likeCount; i++) {
            likedByList.add(likedBy.getString(i));
        }

        Boolean liked;
        if (likedByList.contains(userId)) {
            likedByList.remove(userId);
            likeCount = likedByList.size();
            liked = false;
            Log.d(TAG, "likePostHandler: user unliked the post");
            put(LIKED_BY, likedByList);
            put(LIKE_COUNT, likeCount);
        } else {
            likedByList.add(userId);
            likeCount = likedByList.size();
            liked = true;
            Log.d(TAG, "likePostHandler: user liked the post");
            put(LIKED_BY, likedByList);
            put(LIKE_COUNT, likeCount);
        }


        saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);

                    // Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");

            }
        });

    }

    public Boolean isLiked(String userId) throws JSONException {
        JSONArray likedBy = getJSONArray(LIKED_BY);
        List<String> likedByList = new ArrayList<>();
       // String userId = user.getObjectId();

        for (int i = 0; i < likedBy.length(); i++) {
            likedByList.add(likedBy.getString(i));
        }

        Boolean liked;
        if (likedByList.contains(userId)) {

            liked = true;

        } else {

            liked = false;

        }
        return liked;
    }


}
