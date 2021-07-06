package com.example.fbuinstagram.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.models.Post;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Post> posts;

    // 1
    public GridAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.posts = posts;
    }

    // 2
    @Override
    public int getCount() {
        return posts.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView dummyTextView = new TextView(mContext);
//
//
//        dummyTextView.setText(String.valueOf(position));

        ImageView postImage = new ImageView(mContext);
        //postImage.setMaxHeight(125);
        Glide.with(mContext).load(posts.get(position).getImage().getUrl()).fitCenter().into(postImage);
        return postImage;
    }

}


