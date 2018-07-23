package com.example.arafatm.anti_socialmedia.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arafatm.anti_socialmedia.Models.Post;
import com.example.arafatm.anti_socialmedia.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private List<Post> mPosts;

    Context context;

    public PostAdapter(List<Post> posts){
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_feed, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder viewHolder, int position) {
        // get the data according to this position
        Post post = mPosts.get(position);

        // not a default function for "fullName"
        String fullname = "something";

        ParseUser sender1 = null;

        //added this because debugger asked us to
        try {
            sender1 = post.getSender().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sender = sender1.getString("fullName");
        String message = post.getMessage();

        //populate the views according to this (user name and body)
        viewHolder.tvPostText.setText(message);
        viewHolder.tvFullName.setText(sender);


//        Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPost); <== for image
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFullName;
        public TextView tvPostText;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewBtId lookups
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullNameFeed);
            tvPostText = (TextView) itemView.findViewById(R.id.tvPostBody);

        }
     }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
