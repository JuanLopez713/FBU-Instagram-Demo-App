package com.example.fbuinstagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.util.Log;

import com.example.fbuinstagram.R;
import com.example.fbuinstagram.adapters.PostsAdapter;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private static final String TAG = "FeedActivity";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rvPosts = findViewById(R.id.rvPosts);
        // initialize the array that will hold posts and create a PostsAdapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
        queryPosts();
    }
    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        allPosts.clear();
        adapter.notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        allPosts.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            public void onSuccess(JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                clear();
                // ...the data has come back, add new items to your adapter...
                //allPosts.addAll(posts);
                queryPosts();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
//            }
//
//            public void onFailure(Throwable e) {
//                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
//            }
//        });
   // }
}

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors 
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
               // Log.i(TAG, "done: " + posts);
                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {

                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername()+", date: " + post.getCreatedAt());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}