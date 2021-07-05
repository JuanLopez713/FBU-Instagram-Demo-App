package com.example.fbuinstagram.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.fragments.FeedFragment;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.example.parstagram.FragmentController {
    private static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       toHomeFragment();


    }

    @Override
    public void toHomeFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new FeedFragment()).commit();
    }

    @Override
    public void toPostFragment() {

    }

    @Override
    public void toProfileFragment(ParseUser user) {

    }

    @Override
    public void signOut() {

    }

    @Override
    public void toPostDetailsFragment(Post post) {

    }


}