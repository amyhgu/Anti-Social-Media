package com.example.arafatm.anti_socialmedia.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_BODY = "body";
    private static final String KEY_CREATEDAT = "createdAt";

    public String getMessage() {
        return getString(KEY_BODY);
    }

    public void setMessage(String message) {
        put(KEY_BODY, message);
    }

    public ParseUser getSender() {
        return getParseUser(KEY_SENDER);
    }

    public void setUser(ParseUser sender) {
        put(KEY_SENDER, sender);
    }

    public ParseObject getRecipient() {
        return getParseObject(KEY_RECIPIENT);
    }

    public void setRecipient(Group recipient) {
        put(KEY_RECIPIENT, recipient);
    }

    /*gets time maessage was created*/
    public Date getCreatedAt() {
        return getDate(KEY_CREATEDAT);
    }
}
