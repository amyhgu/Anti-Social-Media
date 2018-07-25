package com.example.arafatm.anti_socialmedia.Util;

import com.parse.ParseUser;

public interface ClickListener {
    void onUsernameClicked(ParseUser user);
    void onPropicClicked(ParseUser user);
}
