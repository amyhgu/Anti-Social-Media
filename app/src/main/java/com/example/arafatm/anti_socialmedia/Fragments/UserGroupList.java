package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arafatm.anti_socialmedia.R;
import com.example.arafatm.anti_socialmedia.Util.GroupListAdapter;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserGroupList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserGroupList#newInstance} factory method to
 * create an instance of this fragment.
 */

public class UserGroupList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GroupListAdapter groupAdapter;
    ArrayList<ParseObject> groupList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Video
    private static final int VIDEO_CAPTURE = 101;
    Uri videoUri;
    View mainView;


    public UserGroupList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserGroupList newInstance(String param1, String param2) {
        UserGroupList fragment = new UserGroupList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            String storyId = getArguments().getString(ARG_PARAM1);
            Toast.makeText(getContext(), storyId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_group_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void navigate_to_fragment(Fragment fragment);
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        groupList = new ArrayList<>();
//        GridView gridview = (GridView) view.findViewById(R.id.gv_group_list);
//        ImageView add_group = (ImageView) view.findViewById(R.id.ic_add_icon);
//
//        add_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Adding a new group", Toast.LENGTH_SHORT).show();
//                /*Navigates to the groupManagerFragment*/
//                Fragment fragment = new GroupCreationFragment();
//                mListener.navigate_to_fragment(fragment);
//            }
//        });
//
//        loadAllGroups(view, gridview);
//    }
//
//    /*loads all groups from parse and display it*/
//    private void loadAllGroups(final View view, final GridView gridview) {
//        final Group.Query postQuery = new Group.Query();
//        postQuery.findInBackground(new FindCallback<Group>() {
//            @Override
//            public void done(final List<Group> objects, ParseException e) {
//                if (e == null) {
//
//
//                }
//            });
//    }
//
//    private void displayRecyckerView(List<Group> objects, View view, GridView gridview) {
//
//    }

    //TODO
    //customize vieeoview
    //save story
    //implement next and prev
    //add caption

}

