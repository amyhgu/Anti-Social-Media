package com.example.arafatm.anti_socialmedia.Models;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Comment extends ParseObject{

    public ParseUser getUser(){
        return getParseUser("user");
    }

    public void setUser(ParseUser parseUser){
        put("user", parseUser);
    }

    public String getComment(){
        return getString("comment");
    }

    public void setComment(String parseFile){
        put("comment", parseFile);
    }
}
