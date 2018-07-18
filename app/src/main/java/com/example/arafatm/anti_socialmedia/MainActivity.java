package com.example.arafatm.anti_socialmedia;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.arafatm.anti_socialmedia.Fragments.ChatFragment;
import com.example.arafatm.anti_socialmedia.Fragments.GameFragment;
import com.example.arafatm.anti_socialmedia.Fragments.GroupFragment;
import com.example.arafatm.anti_socialmedia.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements ChatFragment.OnFragmentInteractionListener, GroupFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);//Initiates BottomNavigationView

        final FragmentManager fragmentManager = getSupportFragmentManager(); //Initiates FragmentManager

        /*gets instance of all fragments here*/
        final Fragment chatFragment = new ChatFragment();
        final Fragment groupFragment = new GroupFragment();
        final Fragment gameFragment = new GameFragment();
        final Fragment settingsFragment = new SettingsFragment();

        // handle navigation selection to various fragments
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_chat_empty:
                                FragmentTransaction fragmentTransactionOne = fragmentManager.beginTransaction(); //creates new transaction
                                fragmentTransactionOne.replace(R.id.fragment_container, chatFragment).commit(); //replace the current page with the specified fragment
                                return true;
                            case R.id.ic_group_empty:
                                FragmentTransaction fragmentTransactionTwo = fragmentManager.beginTransaction();
                                fragmentTransactionTwo.replace(R.id.fragment_container, groupFragment).commit();
                                return true;
                            case R.id.ic_game_empty:
                                FragmentTransaction fragmentTransactionThree = fragmentManager.beginTransaction();
                                fragmentTransactionThree.replace(R.id.fragment_container, gameFragment).commit();
                                return true;
                            case R.id.ic_menu_thin:
                                FragmentTransaction fragmentTransactionFour = fragmentManager.beginTransaction();
                                fragmentTransactionFour.replace(R.id.fragment_container, settingsFragment).commit();
                                return true;

                            default:
                                return false;
                        }
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
