package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
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
import com.example.arafatm.anti_socialmedia.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class GroupSettingsFragment extends Fragment {
    private EditText etGroupName;
    private Button btSave;
    private List<String> newMembers;
    private Group currentGroup;

    private GroupSettingsFragment.OnFragmentInteractionListener mListener;

    public GroupSettingsFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void navigate_to_fragment(Fragment fragment);
    }

    public static GroupSettingsFragment newInstance(Group group) {
        GroupSettingsFragment fragment = new GroupSettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Group.class.getSimpleName(), Parcels.wrap(group));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupSettingsFragment.OnFragmentInteractionListener) {
            mListener = (GroupSettingsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentGroup = Parcels.unwrap(getArguments().getParcelable(Group.class.getSimpleName()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etGroupName = view.findViewById(R.id.etGroupName);
        btSave = view.findViewById(R.id.btSave);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGroupSettings();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateGroupSettings() {
        String newName = etGroupName.getText().toString();
        currentGroup.setGroupName(newName);
        currentGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                GroupFeedFragment groupFeedFragment = GroupFeedFragment.newInstance(currentGroup.getObjectId());
                mListener.navigate_to_fragment(groupFeedFragment);
            }
        });
    }
}
