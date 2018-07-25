package com.example.arafatm.anti_socialmedia.Home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.api.conversation.ApplozicMqttIntentService;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.api.people.ChannelInfo;
import com.applozic.mobicomkit.api.people.UserIntentService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.feed.ChannelFeedApiResponse;
import com.applozic.mobicomkit.uiwidgets.async.AlChannelCreateAsyncTask;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.applozic.mobicomkit.uiwidgets.conversation.MobiComKitBroadcastReceiver;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.MobiComKitActivityInterface;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.ConversationFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComQuickConversationFragment;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;
import com.example.arafatm.anti_socialmedia.Authentification.LoginActivity;
import com.example.arafatm.anti_socialmedia.Fragments.ChatFragment;
import com.example.arafatm.anti_socialmedia.Fragments.GroupCreationFragment;
import com.example.arafatm.anti_socialmedia.Fragments.GroupFeedFragment;
import com.example.arafatm.anti_socialmedia.Fragments.GroupManagerFragment;
import com.example.arafatm.anti_socialmedia.Fragments.ProfileFragment;
import com.example.arafatm.anti_socialmedia.Fragments.SettingsFragment;
import com.example.arafatm.anti_socialmedia.Fragments.UserGroupList;
import com.example.arafatm.anti_socialmedia.storyActivity;
import com.example.arafatm.anti_socialmedia.Models.Group;
import com.example.arafatm.anti_socialmedia.Fragments.StoryFragment;
import com.example.arafatm.anti_socialmedia.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ChatFragment.OnFragmentInteractionListener,
        GroupManagerFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
         UserGroupList.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, GroupCreationFragment.OnFragmentInteractionListener,
        GroupFeedFragment.OnFragmentInteractionListener, MessageCommunicator, MobiComKitActivityInterface {

    // for chat fragment
    private static int retry;
    ConversationUIService conversationUIService;
    MobiComQuickConversationFragment mobiComQuickConversationFragment;
    MobiComKitBroadcastReceiver mobiComKitBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);//Initiates BottomNavigationView
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.INVISIBLE);

        final FragmentManager fragmentManager = getSupportFragmentManager(); //Initiates FragmentManager

        /*gets instance of all fragments here*/

        final Fragment chatFragment = new ChatFragment();
        final Fragment groupFragment = new GroupManagerFragment();
        final Fragment storyFragement = new UserGroupList();
        final Fragment settingsFragment = new SettingsFragment();

        // handle navigation selection to various fragments
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_chat_empty:
                                addFragment(MainActivity.this, mobiComQuickConversationFragment,
                                        ConversationUIService.QUICK_CONVERSATION_FRAGMENT);
                                return true;
                            case R.id.ic_group_empty:
                                FragmentTransaction fragmentTransactionTwo = fragmentManager.beginTransaction();
                                fragmentTransactionTwo.replace(R.id.layout_child_activity, groupFragment).commit();
                                return true;
                            case R.id.ic_story:
                                Intent intent = new Intent(MainActivity.this, storyActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.ic_menu_thin:
                                FragmentTransaction fragmentTransactionFour = fragmentManager.beginTransaction();
                                fragmentTransactionFour.replace(R.id.layout_child_activity, settingsFragment).commit();
                                return true;

                            default:
                                return false;
                        }
                    }
                });


        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                ApplozicClient.getInstance(context).hideChatListOnNotification();
//                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
//                intent.putExtra(ConversationUIService.USER_ID, "receiveruserid123");
//                intent.putExtra(ConversationUIService.DISPLAY_NAME, "Friend McFrienderson"); //put it for displaying the title.
//                intent.putExtra(ConversationUIService.TAKE_ORDER,false); //Skip chat list for showing on back press
//                startActivity(intent);
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                // If any failure in registration the callback  will come here
            }
        };

//        ParseUser.logInInBackground("1", "1", new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if(e == null ){          //if there's no errors
//                    Log.d("LoginActivity", "Login successful!");
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
//                    Log.e("LoginActivity", "Login failure.");
//                    e.printStackTrace();
//                }
//            }
//        });

        ParseUser parseUser = ParseUser.getCurrentUser();
        String userId = parseUser.getObjectId();
        String displayName = parseUser.getString("fullName");
        String email = parseUser.getEmail();

        // login user for Chat Fragment
        User user = new User();
        user.setUserId(userId); //userId it can be any unique user identifier
        user.setDisplayName(displayName); //displayName is the name of the user which will be shown in chat messages
        user.setEmail(email); //optional
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());
        user.setPassword(""); //optional, leave it blank for testing purpose,
        new UserLoginTask(user, listener, this).execute((Void) null);


        // chat fragment setup
        mobiComQuickConversationFragment = new MobiComQuickConversationFragment();
        conversationUIService = new ConversationUIService(this, mobiComQuickConversationFragment);
        mobiComKitBroadcastReceiver = new MobiComKitBroadcastReceiver(this, conversationUIService);

        Intent lastSeenStatusIntent = new Intent(this, UserIntentService.class);
        lastSeenStatusIntent.putExtra(UserIntentService.USER_LAST_SEEN_AT_STATUS, true);
        startService(lastSeenStatusIntent);

        addGroups(parseUser);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_chat_fragment, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onViewProfileSelected() {
        ProfileFragment profileFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
        navigate_to_fragment(profileFragment);
    }

    @Override
    /*Navigates to the groupManagerFragment*/
    public void navigate_to_fragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_child_activity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void startUserChat(String contactName, String message) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationUIService.USER_ID, contactName);
        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
        intent.putExtra(ConversationUIService.DISPLAY_NAME, contactName);
        startActivity(intent);
    }

    public void startGroupChat(int channelId, String groupName) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationUIService.GROUP_ID, channelId);
        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
        intent.putExtra(ConversationUIService.GROUP_NAME, groupName);
        startActivity(intent);
    }

    /* From Chat Fragment tutorial */
    public static void addFragment(FragmentActivity fragmentActivity, Fragment fragmentToAdd, String fragmentTag) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = supportFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.layout_child_activity, fragmentToAdd,
                fragmentTag);

//        if (supportFragmentManager.getBackStackEntryCount() > 1) {
//            supportFragmentManager.popBackStackImmediate();
//        }
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    /* Overrides from Chat Fragment tutorial */
    @Override
    public void onQuickConversationFragmentItemClick(View view, Contact contact, Channel channel, Integer conversationId, String searchString) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationUIService.CONVERSATION_ID, conversationId);
        intent.putExtra(ConversationUIService.SEARCH_STRING, searchString);
        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
        if (contact != null) {
            intent.putExtra(ConversationUIService.USER_ID, contact.getUserId());
            intent.putExtra(ConversationUIService.DISPLAY_NAME, contact.getDisplayName());
            startActivity(intent);
        } else if (channel != null) {
            intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
            intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
            startActivity(intent);

        }
    }
    @Override
    public void startContactActivityForResult() {
        conversationUIService.startContactActivityForResult();
    }

    @Override
    public void addFragment(ConversationFragment conversationFragment) {
        //NOT REQUIRED HERE..
    }

    @Override
    public void updateLatestMessage(Message message, String number) {
        conversationUIService.updateLatestMessage(message, number);
    }

    @Override
    public void removeConversation(Message message, String number) {
        conversationUIService.removeConversation(message, number);

    }

    @Override
    public void showErrorMessageView(String errorMessage) {

    }
    @Override
    public void retry() {
        retry++;
    }
    @Override
    public int getRetryCount() {
        return retry;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mobiComKitBroadcastReceiver, BroadcastService.getIntentFilter());
        Intent subscribeIntent = new Intent(this, ApplozicMqttIntentService.class);
        subscribeIntent.putExtra(ApplozicMqttIntentService.SUBSCRIBE, true);
        startService(subscribeIntent);

        if (!Utils.isInternetAvailable(this)) {
            String errorMessage = getResources().getString(R.string.internet_connection_not_available);
            showErrorMessageView(errorMessage);
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mobiComKitBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        final String deviceKeyString = MobiComUserPreference.getInstance(this).getDeviceKeyString();
        final String userKeyString = MobiComUserPreference.getInstance(this).getSuUserKeyString();
        Intent intent = new Intent(this, ApplozicMqttIntentService.class);
        intent.putExtra(ApplozicMqttIntentService.USER_KEY_STRING, userKeyString);
        intent.putExtra(ApplozicMqttIntentService.DEVICE_KEY_STRING, deviceKeyString);
        startService(intent);
    }

    private void addGroups(ParseUser user) {
//        List<String> groupIds = user.getList("groups");
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Group");
//        query.whereContainedIn("objectId", groupIds);
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < objects.size(); i++) {
//                        ParseObject currentGroup = objects.get(i);
//                        createGroup(currentGroup);
//                    }
//                }
//            }
//        });

        Group.Query groupQuery = new Group.Query();
        groupQuery.whereEqualTo("groupName", "Group4");

        groupQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject currentGroup = objects.get(i);
                        createGroup(currentGroup);
                    }
                }
            }
        });
    }

    private void createGroup(ParseObject group) {
        AlChannelCreateAsyncTask.TaskListenerInterface channelCreateTaskListener = new AlChannelCreateAsyncTask.TaskListenerInterface() {
            @Override
            public void onSuccess(Channel channel, Context context) {
                Log.i("Group","Group response :"+channel);

            }

            @Override
            public void onFailure(ChannelFeedApiResponse channelFeedApiResponse, Context context) {

            }
        };


        List<String> groupMembersUserIdList = new ArrayList<>();
        groupMembersUserIdList.add("Jane Doe");
        groupMembersUserIdList.add("John Smith");//Note:while creating group exclude logged in userId from list
        ChannelInfo channelInfo = new ChannelInfo(group.getString("groupName"),groupMembersUserIdList);
        channelInfo.setType(Channel.GroupType.PUBLIC.getValue().intValue()); //group type
        channelInfo.setImageUrl(group.getParseFile("groupImage").getUrl()); //pass group image link URL
        //channelInfo.setChannelMetadata(channelMetadata); //Optional option for setting group meta data
        channelInfo.setClientGroupId(Integer.toString(GroupFeedFragment.convert(group.getObjectId()))); //Optional if you have your own groupId then you can pass here

        AlChannelCreateAsyncTask channelCreateAsyncTask = new AlChannelCreateAsyncTask(
                this,channelInfo,channelCreateTaskListener);
        channelCreateAsyncTask.execute();
    }

}
