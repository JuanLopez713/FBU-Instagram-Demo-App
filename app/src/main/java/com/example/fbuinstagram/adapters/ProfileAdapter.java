package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfileAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvCreatedAt;
        private TextView tvUsernameSmall;
        private TextView tvLikeCount;
        private ImageView ivProfilePic;
        private ImageButton btnLike;
        private Boolean isLiked;
        private String userId;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvUsernameSmall = itemView.findViewById(R.id.tvUsernameSmall);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            btnLike = itemView.findViewById(R.id.btnLike);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            String username = post.getUser().getUsername();
            tvUsername.setText(username);
            tvUsernameSmall.setText(username);
            userId = ParseUser.getCurrentUser().getObjectId();
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
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        post.likePostHandler(ParseUser.getCurrentUser());
                        int likes = post.getInt("likeCount");
                        tvLikeCount.setText(String.format("%s Likes", likes));
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
