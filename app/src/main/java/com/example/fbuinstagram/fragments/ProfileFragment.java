package com.example.fbuinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.fbuinstagram.activities.LoginActivity;
import com.example.fbuinstagram.adapters.ProfileAdapter;

import com.example.fbuinstagram.databinding.FragmentProfileBinding;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = "ProfileFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ParseUser user;
    FragmentProfileBinding binding;
    RecyclerView rvPosts;
    List<Post> profilePosts;
    ProfileAdapter adapter;
    Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = binding.rvPosts;
        btnLogout = binding.btnLogout;
        // initialize the array that will hold posts and create a PostsAdapter
        profilePosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), profilePosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // query posts from Parstagram
        //queryPosts();
        if (ParseUser.getCurrentUser() != null) {
            user = ParseUser.getCurrentUser();
            String userId = user.getObjectId();
            Log.d(TAG, "userID: " + userId);
            queryProfilePosts(userId);
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

    public void queryProfilePosts(String userObjectId) {
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
                    profilePosts.addAll(posts);
                    adapter.notifyDataSetChanged();
//                    if (posts.size() > 0) {
//
////                        Log.d(TAG, "posts received = " + objects.toString());
////                        for(int i = 0; i < objects.size(); i++){
////                            postImages.add(objects.get(i).getImage());
////                        }
//
                    //}
                } else {
                    Log.e(TAG, "failed getting posts for profile!");
                }
            }
        });
    }
}