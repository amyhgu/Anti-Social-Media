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

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private List<Post> mComments;
    Context context;

    public CommentAdapter(List<Post> comments){
        mComments = comments;
    }

    //create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserComment;
        public TextView tvBodyComment;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewBtId lookups
            tvUserComment = itemView.findViewById(R.id.tvCommentUser);
            tvBodyComment = itemView.findViewById(R.id.tvCommentBody);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View commentView = inflater.inflate(R.layout.item_comment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(commentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post comment = mComments.get(i);
        String body = "";
        String user = "";

        try {
            body = comment.fetchIfNeeded().getString("comment");
            user = comment.getSender().fetchIfNeeded().toString();
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        viewHolder.tvBodyComment.setText(body);
        viewHolder.tvUserComment.setText(user);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
