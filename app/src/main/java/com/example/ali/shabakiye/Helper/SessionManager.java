package com.example.ali.shabakiye.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.ali.shabakiye.MainActivity;

import java.util.HashMap;

/**
 * Created by ali on 7/16/18.
 */

public class SessionManager {
    private static final String TAG = "SessionManager";
    private SharedPreferences pref;
    private Editor editor;
    private Context context;
    private static final int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "session";
    public static final String KEY_USERNAME = "session_username";
    private static final String KEY_PHONE = "session_phone";
    public static final String IS_LOGIN = "session_is_login";

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        this.context = context;
    }

    public void createLoginSession(String username, String phone) {
        editor.putString(IS_LOGIN, "true");
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, ""));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, ""));
        user.put(IS_LOGIN, pref.getString(IS_LOGIN, "false"));

        return user;
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, MainActivity.class);
        //clear all activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //add new flag to start new activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
