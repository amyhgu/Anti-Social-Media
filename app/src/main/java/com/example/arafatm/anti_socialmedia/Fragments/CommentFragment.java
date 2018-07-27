package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arafatm.anti_socialmedia.Models.Post;
import com.example.arafatm.anti_socialmedia.R;
import com.example.arafatm.anti_socialmedia.Util.CommentAdapter;
import com.example.arafatm.anti_socialmedia.Util.PostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment{
    private ParseUser user;
    private Context mContext;
    private String bundleComment;
    private Post originalPost;
    private Post newComment;

    private Button btCommentSubmit;
    private EditText etCommentText;
    private RecyclerView rvComments;
    private SwipeRefreshLayout swipeRefreshLayout;

    CommentAdapter commentAdapter;
    ArrayList<Post> comments;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        originalPost = Parcels.unwrap(getArguments().getParcelable(Post.class.getSimpleName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set up ArrayList of pointers to comments
        final ArrayList<Post> pointToComment = originalPost.getComments();

        btCommentSubmit = view.findViewById(R.id.btCommentPost);
        etCommentText = view.findViewById(R.id.etComment);
        rvComments = view.findViewById(R.id.rvComments);

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(pointToComment);

        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.commentSwipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        createComment(pointToComment, originalPost);

    }

    public static CommentFragment newInstance(Post post) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable(Post.class.getSimpleName(), Parcels.wrap(post));
        commentFragment.setArguments(args);
        return commentFragment;
    }

    private void createComment(final ArrayList<Post> pointToComment, final Post parentPost){
        btCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentString = etCommentText.getText().toString();
                Post comment = new Post();

                // save comment to Parse
                comment.setUser(ParseUser.getCurrentUser());
                comment.setCommentString(commentString);
                comment.saveInBackground();

                pointToComment.add(comment);
                parentPost.setComments(pointToComment);

                etCommentText.setText("");

                Toast.makeText(getContext(), "Comment posted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshFeed(){
        PostAdapter adapter = new PostAdapter(comments);

        adapter.clear();
        loadTopPosts();
        rvComments.scrollToPosition(0);
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();     //there's got to be a better way for doing this
        postsQuery.getTop();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {

                    comments.addAll(objects);
                    commentAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    e.printStackTrace();
                }
            }
        });

    }

}