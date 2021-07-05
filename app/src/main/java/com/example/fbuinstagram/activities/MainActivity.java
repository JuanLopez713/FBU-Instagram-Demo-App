package com.example.fbuinstagram.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.databinding.ActivityMainBinding;
import com.example.fbuinstagram.fragments.FeedFragment;
import com.example.fbuinstagram.fragments.PostCreationFragment;
import com.example.fbuinstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       //toHomeFragment();
        bottomNavigationView = binding.bottomNavigation;
//1b.) Sets on click listeners for when items are clicked!
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_post:           //If profile is clicked --? go to ProfileFrag, else --> go to HomeFrag (FOR NOW)
                        toPostFragment();
                        break;
                    default:
                        toHomeFragment();
                        break;
                }
                return true;
            }
        });

        //2.) by default --> HomeFrag!
        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

    @Override
    public void toHomeFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new FeedFragment()).commit();
    }

    @Override
    public void toPostFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new PostCreationFragment()).commit();
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