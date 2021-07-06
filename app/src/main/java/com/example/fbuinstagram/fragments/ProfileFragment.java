package com.example.fbuinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuinstagram.activities.LoginActivity;
import com.example.fbuinstagram.adapters.GridAdapter;
import com.example.fbuinstagram.adapters.ProfileAdapter;

import com.example.fbuinstagram.databinding.FragmentProfileBinding;
import com.example.fbuinstagram.models.Post;

import com.parse.ParseFile;

import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;


import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";


    ParseUser user;
    FragmentProfileBinding binding;
    RecyclerView rvPosts;
    List<Post> profilePosts;
    ProfileAdapter adapter;
    Button btnLogout;
    TextView tvNoImagesWarning;
    TextView tvPostsValue;
    ImageView ivProfilePic;
    TextView tvBio;
    TextView tvUsername;
    GridView gvPostGrid;
    GridAdapter gridAdapter;


    public ProfileFragment(ParseUser user, List<Post> profilePosts) {
        // Required empty public constructor
        this.user = user;
        this.profilePosts = profilePosts;

    }



    public static ProfileFragment newInstance(ParseUser user, List<Post> profilePosts) {
        ProfileFragment fragment = new ProfileFragment(user, profilePosts);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = binding.rvPosts;
        btnLogout = binding.btnLogout;
        tvNoImagesWarning = binding.tvNoImagesWarning;
        tvPostsValue = binding.tvPostsValue;
        ivProfilePic = binding.ivUserProfile;
        tvBio = binding.tvBio;
        tvUsername = binding.tvUsername;
        gvPostGrid = binding.gvPostGrid;

        boolean isGridView = true;
        if(user != ParseUser.getCurrentUser()){
            btnLogout.setVisibility(View.GONE);
        } else{
            btnLogout.setVisibility(View.VISIBLE);
        }
        // initialize the array that will hold posts and create a PostsAdapter
        //profilePosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), profilePosts);

        Log.d(TAG, "onViewCreated: " + profilePosts);
        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        gridAdapter = new GridAdapter(getContext(), profilePosts );
        gvPostGrid.setAdapter(gridAdapter);
        if (user != null) {

            ParseFile image = user.getParseFile("profilePicture");
            tvUsername.setText(user.getUsername());
            if (image != null) {
                Log.d(TAG, "Found your profile picture!");
                Glide.with(getContext()).load(image.getUrl()).centerCrop()
                        .transform(new CircleCrop()).into(ivProfilePic);
            }

            String userId = user.getObjectId();
            Log.d(TAG, "userID: " + userId + " name: " + user.getUsername() + " bio: " + user.getString("bio"));
            tvBio.setText(user.getString("bio"));
            tvPostsValue.setText(String.valueOf(profilePosts.size()));
            if (profilePosts.size() > 0) {

                if(isGridView){
                    rvPosts.setVisibility(View.GONE);
                    gvPostGrid.setVisibility(View.VISIBLE);
                }else{
                    rvPosts.setVisibility(View.VISIBLE);
                    gvPostGrid.setVisibility(View.GONE);
                }
//                rvPosts.setVisibility(View.VISIBLE);
                tvNoImagesWarning.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                gvPostGrid.setVisibility(View.GONE);
                rvPosts.setVisibility(View.GONE);
                tvNoImagesWarning.setVisibility(View.VISIBLE);
            }



        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        ParseUser.logOut();
        user = ParseUser.getCurrentUser(); // should be null but isn't...
        //invalidateOptionsMenu();
        Toast.makeText(getContext(), "Disconnected...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

}