package com.example.arafatm.anti_socialmedia.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {
    private static final String KEY_USERS = "users";
    private static final String KEY_POSTS = "post";
    private static final String KEY_NAME = "groupName";
    private static final String KEY_IMAGE = "groupImage";
    
    public String getGroupName() {
        return getString(KEY_NAME);
    }

    public void setGroupName(String name) {
        put(KEY_NAME, name);
    }

    public ParseFile getGroupImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setGroupImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public List<ParseUser> getUsers() {
        return getList(KEY_USERS);
    }

    public void addUsers(ParseUser user) {
        List<ParseUser> users = getList(KEY_USERS);
        users.add(user);
        put(KEY_USERS, users);
    }

    public List<ParseObject> getPosts() {
        return getList(KEY_POSTS);
    }

    public void addPosts(Post post) {
        List<ParseObject> users = getList(KEY_POSTS);
        users.add(post);
        put(KEY_POSTS, users);
    }
}
