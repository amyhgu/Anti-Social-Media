package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arafatm.anti_socialmedia.R;
import com.facebook.Profile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ParseUser user;
    private ImageView ivPropic;
    private TextView tvFullName;
    private Context mContext;

    public static ProfileFragment newInstance(ParseUser user) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ParseUser.class.getSimpleName(), Parcels.wrap(user));
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        user = Parcels.unwrap(getArguments().getParcelable(ParseUser.class.getSimpleName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvFullName.setText(user.getString("fullName"));

        ivPropic = view.findViewById(R.id.ivPropic);
        String propicUrl = user.getString("propicUrl");
        propicUrl = (propicUrl == null) ? user.getParseFile("profileImage").getUrl() : propicUrl;
        Glide.with(mContext).load(propicUrl).into(ivPropic);
    }
}
