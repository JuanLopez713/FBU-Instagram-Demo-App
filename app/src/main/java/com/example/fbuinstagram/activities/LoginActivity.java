package com.example.fbuinstagram.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.databinding.ActivityLoginBinding;
import com.example.fbuinstagram.databinding.ActivityMainBinding;
import com.example.fbuinstagram.fragments.FeedFragment;
import com.example.fbuinstagram.fragments.LoginFragment;
import com.example.fbuinstagram.fragments.SignUpFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements com.example.parstagram.LoginController {
    private static final String TAG = "LoginActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //1.) Set up the toolbar for this frag:
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar supportActionBar = (this.getSupportActionBar());
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setLogo(R.drawable.nux_dayone_landing_logo);
       toLoginFragment();
    }




    public void toLoginFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
    }


    public void toSignUpFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new SignUpFragment()).commit();
    }



}