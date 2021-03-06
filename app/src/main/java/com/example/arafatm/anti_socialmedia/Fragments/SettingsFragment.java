package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arafatm.anti_socialmedia.Authentification.LoginActivity;
import com.example.arafatm.anti_socialmedia.Models.GroupRequestNotif;
import com.example.arafatm.anti_socialmedia.R;
import com.example.arafatm.anti_socialmedia.Util.NotifsAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Context mContext;
    private String mParam1;
    private String mParam2;
    private Button logOutBtn;
    private ImageView ivPropic;
    private TextView tvFullName;
    private TextView tvViewProfile;
    private RelativeLayout rlViewProfile;
    private RecyclerView rvNotifs;
    private ArrayList<GroupRequestNotif> requestList;
    private NotifsAdapter requestAdapter;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
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
        void onViewProfileSelected();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        rvNotifs = view.findViewById(R.id.rvNotifs);

        requestList = new ArrayList<>();
        getGroupRequests();

        requestAdapter = new NotifsAdapter(requestList);
        rvNotifs.setAdapter(requestAdapter);
        rvNotifs.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logOutBtn = view.findViewById(R.id.log_out_button);
        ivPropic = view.findViewById(R.id.ivGroupPic);
        tvFullName = view.findViewById(R.id.tvGroupName);
        tvViewProfile = view.findViewById(R.id.tvViewProfile);
        rlViewProfile = view.findViewById(R.id.rlViewProfile);

        ParseUser user = ParseUser.getCurrentUser();

        // for Parse profile pictures
        String propicUrl = user.getString("propicUrl");
        if (propicUrl != null && !(propicUrl.equals("")))  {
            Glide.with(mContext).load(propicUrl).into(ivPropic);
        }
        else if(user.getParseFile("profileImage") != null){
            Glide.with(mContext).load(user.getParseFile("profileImage").getUrl()).into(ivPropic);
        }
        tvFullName.setText(user.getString("fullName"));

        logOutBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsFragment.this.getContext())
                        .setMessage("Do you want to log out?")
                        .setTitle("Log Out")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();

                                //close the dialog
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        rlViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewProfileSelected();
            }
        });
    }

    public void getGroupRequests() {
        GroupRequestNotif.Query query = new GroupRequestNotif.Query();
        ParseUser currentUser = ParseUser.getCurrentUser();
        query.getInvitesReceived(currentUser).withAll();

        query.findInBackground(new FindCallback<GroupRequestNotif>() {
            @Override
            public void done(List<GroupRequestNotif> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        requestList.add(objects.get(i));
                        requestAdapter.notifyDataSetChanged();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logout() {
        //Check if user is currently logged in
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            LoginManager.getInstance().logOut();
        }

        // This will log out for Parse
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        Intent intent = new Intent(SettingsFragment.this.getContext(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
