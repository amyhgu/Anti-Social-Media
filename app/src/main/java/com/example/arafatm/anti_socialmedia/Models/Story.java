package com.example.arafatm.anti_socialmedia.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Story")
public class Story extends ParseObject {
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_STORY = "story";


    public ParseUser getSender() {
        return getParseUser(KEY_SENDER);
    }

    public void setSender(ParseUser sender) {
        put(KEY_SENDER, sender);
    }

    public ParseObject getRecipient() {
        return getParseObject(KEY_RECIPIENT);
    }

    public void setRecipient(Group recipient) {
        put(KEY_RECIPIENT, recipient);
    }

    //Gets the list of comments from Parse
    public List<ParseObject> getStories() {
        return getList(KEY_STORY);
    }

    /*Gets the Array of stories from Parse, updates it, and save it back to parse*/
    public void addStory(ParseObject story) {
        List<ParseObject> stories = getList(KEY_STORY);
        stories.add(story);
        put(KEY_STORY, stories);
    }
}
