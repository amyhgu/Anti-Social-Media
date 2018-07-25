package com.example.arafatm.anti_socialmedia.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arafatm.anti_socialmedia.R;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private List<Comment> mComments;
    Context context;

    public CommentAdapter(List<Comment> comments){
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

    }

    @Override
    public int getItemCount() {
//        mComments.size();
        return 0;
    }
}
