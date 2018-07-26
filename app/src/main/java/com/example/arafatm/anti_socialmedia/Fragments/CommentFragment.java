package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arafatm.anti_socialmedia.Models.Post;
import com.example.arafatm.anti_socialmedia.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class CommentFragment extends Fragment{
    private ParseUser user;
    private Context mContext;
    private String mParam1;
    private Post post;


    private static final String ARG_PARAM1 = "param1";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        post = Parcels.unwrap(getArguments().getParcelable(Post.class.getSimpleName()));
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    public static CommentFragment newInstance(Post post) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable(Post.class.getSimpleName(), Parcels.wrap(post));
        commentFragment.setArguments(args);
        return commentFragment;
    }

}