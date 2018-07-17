package com.example.arafatm.anti_socialmedia.Util;

import android.app.Application;

import com.example.arafatm.anti_socialmedia.Models.Group;
import com.example.arafatm.anti_socialmedia.Models.Message;
import com.example.arafatm.anti_socialmedia.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Group.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("triple_as_lovely_app")
                .clientKey("amy_alison_arafat_are_triple_as")
                .server("http://anti-social-media.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
