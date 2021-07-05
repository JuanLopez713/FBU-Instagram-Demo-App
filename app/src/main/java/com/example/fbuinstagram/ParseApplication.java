package com.example.fbuinstagram;

import android.app.Application;


import com.example.fbuinstagram.BuildConfig;

import com.example.fbuinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Register your parse com.example.fbuinstagram.activities.models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APP_KEY)
                // if defined
                .clientKey(BuildConfig.CLIENT_KEY)
                .server(BuildConfig.SERVER_URL)
                .build()
        );
    }
}
