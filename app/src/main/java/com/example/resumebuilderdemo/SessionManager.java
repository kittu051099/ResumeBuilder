package com.example.resumebuilderdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_USER_ID = "LOGIN";
    private static final String LOGIN ="IS_LOGIN";
    public static final String USERID ="USERID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_USER_ID, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String UserID){
        editor.putBoolean(LOGIN, true);
        editor.putString(USERID, UserID);
        editor.apply();
    }
    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLogin())
        {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USERID,sharedPreferences.getString(USERID,null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
