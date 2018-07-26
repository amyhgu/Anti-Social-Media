package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arafatm.anti_socialmedia.Models.Group;
import com.example.arafatm.anti_socialmedia.Models.GroupRequestNotif;
import com.example.arafatm.anti_socialmedia.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class GroupCustomizationFragment extends Fragment {
    private EditText etGroupName;
    private Button btCreateGroup;
    private List<String> newMembers;

    private OnFragmentInteractionListener mListener;

    public GroupCustomizationFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void navigate_to_fragment(Fragment fragment);
    }

    public static GroupCustomizationFragment newInstance(ArrayList<String> friendsToAdd) {
        GroupCustomizationFragment fragment = new GroupCustomizationFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("newMembers", friendsToAdd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newMembers = getArguments().getStringArrayList("newMembers");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_customization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etGroupName = view.findViewById(R.id.etGroupName);
        btCreateGroup = view.findViewById(R.id.btCreateGroup);

        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGroupRequest();
            }
        });
    }

    private void sendGroupRequest() {
        //Create new group and initialize it
        final Group newGroup = new Group();
        String newName = etGroupName.getText().toString();
        newGroup.initGroup(newName, newMembers);
        newGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //bundle the group objectId and send to groupfeed fragment for later use
                Bundle args = new Bundle();
                String objectId = newGroup.getObjectId();
                args.putString("param1", objectId);

                /*Navigates to the GroupFeedFragment*/
                Fragment fragment = new GroupFeedFragment();
                fragment.setArguments(args);
                mListener.navigate_to_fragment(fragment);
            }
        });

        ParseUser loggedInUser = ParseUser.getCurrentUser();

        List<ParseObject> currentGroups = loggedInUser.getList("groups");
        if (currentGroups == null) {
            currentGroups = new ArrayList<>();
        }
        currentGroups.add(newGroup);
        loggedInUser.put("groups", currentGroups);
        loggedInUser.saveInBackground();

        for (int i = 0; i < newMembers.size(); i++) {
            final GroupRequestNotif newRequest = new GroupRequestNotif();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(newMembers.get(i), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    newRequest.initRequest(object, newGroup);
                    try {
                        newRequest.save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
