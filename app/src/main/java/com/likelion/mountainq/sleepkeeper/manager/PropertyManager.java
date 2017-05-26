package com.likelion.mountainq.sleepkeeper.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.likelion.mountainq.sleepkeeper.helper.ApplicationController;


/**
 * Created by dnay2 on 2017-05-15.
 */

public class PropertyManager {

    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if(instance==null){
            instance = new PropertyManager();
        }
        return instance;
    }
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private PropertyManager(){
        mPrefs =
                PreferenceManager.getDefaultSharedPreferences(ApplicationController.getInstance().getApplicationContext());
        mEditor = mPrefs.edit();
        mEditor.apply();
    }

    private PropertyManager(Context context){
        mPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
        mEditor.apply();
    }

    private static final String GOOGLE_TOKEN = "google_token";
    public String getGoogleToken(){
        return mPrefs.getString(GOOGLE_TOKEN, "null");
    }
    public void setGoogleToken(String googleToken){
        mEditor.putString(GOOGLE_TOKEN, googleToken);
        mEditor.commit();
    }

    private static final String FACEBOOK_TOKEN = "facebook_token";
    public String getFacebookToken() {
        return mPrefs.getString(FACEBOOK_TOKEN, "null");
    }
    public void setFacebookToken(String facebookToken){
        mEditor.putString(FACEBOOK_TOKEN, facebookToken);
        mEditor.commit();
    }

    private static final String LINE_TOKEN = "line_token";
    public String getLineToken(){
        return mPrefs.getString(LINE_TOKEN, "null");
    }
    public void setLineToken(String lineToken){
        mEditor.putString(LINE_TOKEN, lineToken);
        mEditor.commit();
    }

    private static final String PUSH_TOKEN = "push_token";
    public String getPushToken() {
        return mPrefs.getString(PUSH_TOKEN, "null");
    }
    public void setPushToken(String pushToken){
        mEditor.putString(PUSH_TOKEN, pushToken);
        mEditor.commit();
    }

    private static final String PUSH_ACCESS = "pust_access";
    public int getPushAccess(){
        return mPrefs.getInt(PUSH_ACCESS, -1);
    }

    public void setPushAccess(int pushAccess){
        mEditor.putInt(PUSH_ACCESS, pushAccess);
        mEditor.commit();
    }

    public void clear(){
        mEditor.clear();
        mEditor.commit();
    }
}
