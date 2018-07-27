package com.example.arafatm.anti_socialmedia.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arafatm.anti_socialmedia.Models.Group;
import com.example.arafatm.anti_socialmedia.Models.Post;
import com.example.arafatm.anti_socialmedia.R;
import com.example.arafatm.anti_socialmedia.Util.PhotoHelper;
import com.example.arafatm.anti_socialmedia.Util.PostAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String key1 = "111";

    // TODO: Rename and change types of parameters
    //general feed setup
    private String mParam1;
    private String mParam2;
    private String groupObjectId;

    private String groupName;
    private int groupId;
    private Group group;

    private TextView tvGroupName;
    private ImageView ivGroupPic;
    private ImageView ivStartChat;
    private ImageView ivThreeDots;

    private File photoFile;
    public String photoFileName = "photo.jpg";
    PhotoHelper photoHelper;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    //posts

    //for posting
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    private EditText messageInput;
    private Button createButton;
    private SwipeRefreshLayout swipeContainer;

    //list of users

    private OnFragmentInteractionListener mListener;

    public GroupFeedFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void navigate_to_fragment(Fragment fragment);
        void startGroupChat(int groupId, String groupName);
    }

    public static GroupFeedFragment newInstance(String mParam1) {
        GroupFeedFragment fragment = new GroupFeedFragment();
        Bundle args = new Bundle();
        args.putString(key1, mParam1);
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
        Bundle bundle = this.getArguments();

        if (bundle != null) {
             groupObjectId = bundle.getString(ARG_PARAM1, groupObjectId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Equivalent to setContentView
        return inflater.inflate(R.layout.fragment_group_feed, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //creating a post
        final ParseUser postUser = ParseUser.getCurrentUser();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");

        messageInput = view.findViewById(R.id.etNewPost);
        createButton = view.findViewById(R.id.btCreatePost);
        ivStartChat = view.findViewById(R.id.ivStartChat);
        ivThreeDots = view.findViewById(R.id.ivThreeDots);
        rvPosts = view.findViewById(R.id.rvPostsFeed);

        //displaying the posts
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);

        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(GroupFeedFragment.this.getContext()));
        rvPosts.setAdapter(postAdapter);

        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        query.getInBackground(groupObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    group = (Group) object;
                    Toast.makeText(getContext(), object.getString("groupName") + " Successfully Loaded", Toast.LENGTH_SHORT).show();
                    group = (Group) object;

                    tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
                    groupName = object.getString("groupName");
                    tvGroupName.setText(groupName);
                    groupId = convert(object.getObjectId());
                    Log.d("weird", Integer.toString(groupId));

                    ivGroupPic = (ImageView) view.findViewById(R.id.ivCoverPhoto);
                    ParseFile groupImage = object.getParseFile("groupImage");

                    if (groupImage != null) {
                        /*shows group image on gridView*/
                        Glide.with(getContext())
                                .load(groupImage.getUrl())
                                .into(ivGroupPic);
                    }

                    Button button = (Button) view.findViewById(R.id.btAddPostImage);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            uploadImage();
//                            takePhoto();
                            photoHelper = new PhotoHelper(getContext());
                            Intent intent = photoHelper.takePhoto();
                            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                    });

                } else {
                    e.printStackTrace();
                }
            }

        });

        loadTopPosts();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = messageInput.getText().toString();

                createPost(message, postUser, group);

                //TODO: Do i need to create a new instance for groups? This method should require a group
                //I am trying to pass the group so that the post knows what group it's going under

//                final ParseFile parseFile = new ParseFile(textPost);
                //debugger says that there's something wrong around here
                //ParseObject Post that we created

                //save file to parse
                //this is code from Parsetagram, but probably don't need a Parsefile because we aren't posting with an image
//                parseFile.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if(e == null){
//                            createPost(message, user);
//                        }
//                        else{
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
            }
        });

        ivStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.startGroupChat(groupId, groupName);
            }
        });

        ivThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupSettingsFragment groupSettingsFragment = GroupSettingsFragment.newInstance(group);
                mListener.navigate_to_fragment(groupSettingsFragment);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void createPost(final String message, ParseUser creator, Group group){    // +param -> Parsefile imageFile
        final Post newPost = new Post();
        newPost.setMessage(message);
        newPost.setUser(creator);
        newPost.setRecipient(group);
//        newPost.setImage(imageFile);          <== figure out image posting later

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("PostActivity", "Create post success!");
                    Toast.makeText(GroupFeedFragment.this.getContext(), "Posted!", Toast.LENGTH_LONG).show();
                    messageInput.setText("");
                    refreshFeed();
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        // TODO - add the post to the current Group's list/array of Posts
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop();       //<== this gets the post from a specific user. Won't cause harm, but don't need it rn
        // ^ this line originally had ".withUser", so this should fix it

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {

                    postAdapter.notifyDataSetChanged();
                    posts.addAll(objects);

                    swipeContainer.setRefreshing(false);

                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    private void refreshFeed(){
        PostAdapter adapter = new PostAdapter(posts);

        adapter.clear();
        loadTopPosts();
        rvPosts.scrollToPosition(0);
    }

    private void takePhoto() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.antisocialmedia.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "GroupFeedFragment");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("GroupFeedFragment", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri photoUri = data.getData();
                    // Do something with the photo based on Uri
                    Bitmap selectedImage = null;
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    byte[] Data = stream.toByteArray();

                    Toast.makeText(getContext(), "Picture taken!", Toast.LENGTH_SHORT).show();

                    //TODO
                    //add to post
                    //display
                }
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // for converting group objectId to integer (used for chat channel ID)
    // credit to https://stackoverflow.com/questions/30404946/how-to-convert-parse-objectid-string-to-long
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static int convertChar(char c) {
        int ret = CHARS.indexOf( c );
        if (ret == -1)
            throw new IllegalArgumentException( "Invalid character encountered: "+c);
        return ret;
    }

    public static int convert(String s) {
        if (s.length() != 10)
            throw new IllegalArgumentException( "String length must be 10, was "+s.length() );
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            ret = (ret << 6) + convertChar( s.charAt( i ));
        }
        return ret;
    }
}
