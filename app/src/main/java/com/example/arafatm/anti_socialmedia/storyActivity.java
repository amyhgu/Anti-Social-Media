package com.example.arafatm.anti_socialmedia;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.arafatm.anti_socialmedia.Fragments.UserGroupList;
import com.example.arafatm.anti_socialmedia.Models.Story;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class storyActivity extends AppCompatActivity  implements UserGroupList.OnFragmentInteractionListener {
    private Context context;
    private static final int VIDEO_CAPTURE = 101;
    private VideoView mVideoView;
    private String identifier = "videoUri";
    private static final String ARG_PARAM1 = "param1";
    private  Story storyNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = storyActivity.this;
        setContentView(R.layout.activity_story);


        ImageView startCapturing = (ImageView) findViewById(R.id.iv_startCapture);
        ImageView nextStory = (ImageView) findViewById(R.id.iv_next);
        ImageView prevStory = (ImageView) findViewById(R.id.iv_prev);
        VideoView videoView = findViewById(R.id.vv_story);
        ImageView goToAddStory = (ImageView) findViewById(R.id.iv_add_to_group);

        startCapturing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Starting to record story", Toast.LENGTH_LONG).show();
                startRecordingVideo();
            }
        });

        nextStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Next story", Toast.LENGTH_LONG).show();
            }
        });

        prevStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Previous story", Toast.LENGTH_LONG).show();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mVideoView != null) {
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                        Toast.makeText(context, "story on hold", Toast.LENGTH_LONG).show();
                    } else {
                        mVideoView.start();
                        Toast.makeText(context, "story playing", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        goToAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView != null) {
                    //create new groupList fragment
                    Fragment groupList = new UserGroupList();
                    //create a bundle to store the newly created story
                    Bundle args = new Bundle();

                    args.putString(ARG_PARAM1, "here you are"); //pass group objectId
                    groupList.setArguments(args);

                    //navigate to fragment
                    final FragmentManager fragmentManager = getSupportFragmentManager(); //Initiates FragmentManager
                    FragmentTransaction fragmentTransactionThree = fragmentManager.beginTransaction();
                    fragmentTransactionThree.replace(R.id.layout_story_activity, groupList).commit();
                }
            }
        });
    }

    public void startRecordingVideo() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            //intent to fire the camera
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            //create a file to the save path
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");

            //get video URI
            Uri videoUri = FileProvider.getUriForFile(storyActivity.this, "com.antisocialmedia.fileprovider", mediaFile);
            String m = videoUri.toString();
            intent.putExtra(identifier, videoUri);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Start the video capture intent to record video
                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                //gets video Uri
                Uri videoUri = data.getData();
                //save story
                //saveStory(videoUri);
                //plays the story
                playbackRecordedVideo(videoUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveStory(Uri videoUri) {
        //create new story
        storyNew = new Story();
        //set sender
        storyNew.setSender(ParseUser.getCurrentUser());
        //set recipient
//        storyNew.setRecipient(null); //to be set in groupList fragment
        //set story
        storyNew.setStory(videoUri);
        //save story to parse
        storyNew.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("SAVING STORY", "SUCCESSFULLY STORED STORY IN PARSE");
            }
        });

    }

    public void playbackRecordedVideo(Uri videouri) {
        mVideoView = (VideoView) findViewById(R.id.vv_story);
        mVideoView.setVideoURI(videouri);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
