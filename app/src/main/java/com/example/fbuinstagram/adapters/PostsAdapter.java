package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Post;
import com.example.parstagram.FragmentController;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    com.example.parstagram.FragmentController controller;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvUsernameSmall;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvCreatedAt;
        private ImageView ivProfilePic;
        private TextView tvLikeCount;
        private ImageButton btnLike;
        private Boolean isLiked;
        private String userId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsernameSmall = itemView.findViewById(R.id.tvUsernameSmall);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            controller = (FragmentController) context;
            btnLike = itemView.findViewById(R.id.btnLike);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(Post post) {
            // Bind the post data to the view elements
            userId = ParseUser.getCurrentUser().getObjectId();
            tvDescription.setText(post.getDescription());
            String username = post.getUser().getUsername();
            tvUsername.setText(username);
            tvUsernameSmall.setText(username);
            int likes = post.getInt("likeCount");
            tvLikeCount.setText(String.format("%s Likes", likes));
            try {
                isLiked = post.isLiked(userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(isLiked){
                btnLike.setImageResource(R.drawable.ufi_heart_active);
            }else{
                btnLike.setImageResource(R.drawable.ufi_heart);
            }
            tvCreatedAt.setText(post.getTimeCreatedAt(post.getCreatedAt()));

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ParseFile profilePic = post.getUser().getParseFile("profilePicture");
            if (profilePic != null) {
                Glide.with(context).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
            }
            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    controller.toUserFragment(post.getUser());
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        post.likePostHandler(ParseUser.getCurrentUser());
                        int likes = post.getInt("likeCount");
                        tvLikeCount.setText(likes + " Likes");
                        isLiked = !isLiked;
                        if(isLiked){
                            btnLike.setImageResource(R.drawable.ufi_heart_active);
                        }else{
                            btnLike.setImageResource(R.drawable.ufi_heart);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
