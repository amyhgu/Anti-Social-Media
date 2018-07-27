package com.example.arafatm.anti_socialmedia.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.arafatm.anti_socialmedia.R;
// ...

public class CreatePostFragment extends DialogFragment {

    private EditText etNewPost;
    private ImageView ivCamera;
    private ImageView ivUpload;
    private ImageView ivShareFrom;
    private ImageView ivCreatePost;

    public CreatePostFragment() {
        // Empty constructor is required for DialogFragment
    }

    private CreatePostFragment.OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static CreatePostFragment newInstance(String title) {
        CreatePostFragment frag = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etNewPost = view.findViewById(R.id.etNewPost);
        ivCamera = view.findViewById(R.id.ivCamera);
        ivUpload = view.findViewById(R.id.ivUpload);
        ivShareFrom = view.findViewById(R.id.ivShareFrom);
        ivCreatePost = view.findViewById(R.id.ivCreatePost);

        // Show soft keyboard automatically and request focus to field
        etNewPost.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
