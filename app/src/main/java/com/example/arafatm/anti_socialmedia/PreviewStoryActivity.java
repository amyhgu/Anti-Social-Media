package com.example.arafatm.anti_socialmedia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arafatm.anti_socialmedia.Fragments.PictureFragment;
import com.example.arafatm.anti_socialmedia.Fragments.VideoFragment;
import com.example.arafatm.anti_socialmedia.Home.MainActivity;

public class PreviewStoryActivity extends AppCompatActivity implements PictureFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_story);

        ImageView backToCamera = (ImageView) findViewById(R.id.iv_camera);
        ImageView shareButton = (ImageView) findViewById(R.id.iv_share);
        EditText caption = (EditText) findViewById(R.id.tv_caption);

        backToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Navigating to camera page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PreviewStoryActivity.this, StoryActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Sharing story", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PreviewStoryActivity.this, MainActivity.class);
                intent.putExtra("key", "not null");
                startActivity(intent);
            }
        });

        final FragmentManager fragmentManager = getSupportFragmentManager(); //Initiates FragmentManager
        Fragment pictureFragment = new PictureFragment();
        Fragment videoFragment = new VideoFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String intentResult = getIntent().getStringExtra("result");
        Bundle args = new Bundle();
        args.putString("param1", intentResult); //pass group objectId

        //TODO
        //GET either the video or picture and pass it to the right fragment

        //if it is a picture
        if (intentResult.compareTo("picture") == 0) {
             pictureFragment.setArguments(args);
             fragmentTransaction.replace(R.id.fragment_container, pictureFragment).commit();

            //if it is a video
        } else if (intentResult.compareTo("video") == 0) {
             videoFragment.setArguments(args);
             fragmentTransaction.replace(R.id.fragment_container, videoFragment).commit();
        } else {
            //This is wouldn't even happen
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

