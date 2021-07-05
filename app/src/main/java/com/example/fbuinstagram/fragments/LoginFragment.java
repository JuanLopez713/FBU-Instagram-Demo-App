package com.example.fbuinstagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.activities.LoginActivity;
import com.example.fbuinstagram.activities.MainActivity;
import com.example.fbuinstagram.databinding.FragmentFeedBinding;
import com.example.fbuinstagram.databinding.FragmentLoginBinding;
import com.example.parstagram.LoginController;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = "LoginFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentLoginBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    com.example.parstagram.LoginController controller;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView tvSignUp;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginController){
            controller = (LoginController) context;
        }
        else{
            Log.e(TAG, "ERROR:  context passed in did NOT implement FragmentController!");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        tvSignUp = binding.tvSignUp;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.toSignUpFragment();
            }
        });

    }

    private void goMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        //  finish();
    }

    private void loginUser(String username, String password) {
        Log.d(TAG, "Attempting to login user: " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    //TODO handle better error
                    Log.d(TAG, "Issue with login: ", e);
                    return;
                }
                //TODO: navigate to the main activity if the user has signed in properly
                goMainActivity();
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}