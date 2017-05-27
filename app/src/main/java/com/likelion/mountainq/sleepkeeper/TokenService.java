package com.likelion.mountainq.sleepkeeper;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.likelion.mountainq.sleepkeeper.manager.ConnectionTask;
import com.likelion.mountainq.sleepkeeper.manager.PropertyManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class TokenService extends FirebaseInstanceIdService {

    private static final String TAG ="TokenService";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refreshed token" + token);
        PropertyManager.getInstance().setPushToken(token);
        sendToServer(token);
    }

    private void sendToServer(String token){
        ConnectionTask task = new ConnectionTask();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("token", token);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String URL = "https://gyotong-jomno.c9users.io/home/save";
        String message = jsonObject.toString();
        task._post(URL, message);
    }
}
