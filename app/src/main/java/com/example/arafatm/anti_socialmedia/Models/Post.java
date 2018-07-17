package com.example.arafatm.anti_socialmedia.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CREATEDAT = "createdAt";
    private static final String KEY_MEDIA = "media";
    private static final String KEY_COMMENTS = "comments";
    private List<ParseObject> comments;

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_MEDIA);
    }

    public void setImage(ParseFile image) {
        put(KEY_MEDIA, image);
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

    public Date getCreatedAt() {
        return getDate(KEY_CREATEDAT);
    }

    public List<ParseObject> getComments() {
        return getList(KEY_COMMENTS);
    }

    public void addComments(Post comment) {
        comments.addAll(this.<ParseObject>getList(KEY_COMMENTS)); //more efficient way???
        comments.add(comment);
        put(KEY_COMMENTS, comments);
    }
}
