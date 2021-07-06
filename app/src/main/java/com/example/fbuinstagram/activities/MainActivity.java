package com.example.fbuinstagram.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.databinding.ActivityMainBinding;
import com.example.fbuinstagram.fragments.FeedFragment;
import com.example.fbuinstagram.fragments.PostCreationFragment;
import com.example.fbuinstagram.fragments.ProfileFragment;
import com.example.fbuinstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.example.parstagram.FragmentController {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;
    ParseUser currentUser = ParseUser.getCurrentUser();
    List<Post> profilePosts;
    List<Post> userPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profilePosts = new ArrayList<>();
        //1.) Set up the toolbar for this frag:
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar supportActionBar = (this.getSupportActionBar());
        supportActionBar.setDisplayShowHomeEnabled(true);


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
                    case R.id.action_profile:           //If profile is clicked --? go to ProfileFrag, else --> go to HomeFrag (FOR NOW)
                        toProfileFragment(currentUser);
                        break;
                    //If profile is clicked --? go to ProfileFrag, else --> go to HomeFrag (FOR NOW)
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
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new PostCreationFragment(currentUser)).commit();
    }


    public void toProfileFragment(ParseUser userParse) {
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                currentUser = user;
                queryProfilePosts(currentUser);


            }
        });

    }


    public void toUserFragment(ParseUser user) {
        queryProfilePosts(user);
     //   fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment(user, profilePosts)).commit();
    }

    @Override
    public void signOut() {

    }

    @Override
    public void toPostDetailsFragment(Post post) {

    }

    public void queryProfilePosts(ParseUser user) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, user);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "success getting posts for profile!" + posts);

                    profilePosts.clear();
                    profilePosts.addAll(posts);
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment(user, profilePosts)).commit();
                } else {
                    Log.e(TAG, "failed getting posts for profile!");
                }
            }
        });
    }

}