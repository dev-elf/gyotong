package com.likelion.mountainq.sleepkeeper;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        HashMap<String, String> data = new HashMap<>(remoteMessage.getData());
        Log.d(TAG, "order : " + Integer.valueOf(data.get("order")));
        Log.d(TAG, "message : " + String.valueOf(data.get("message")));
        Intent intent = new Intent(MessagingService.this, Pop.class);
        intent.putExtra("order", Integer.valueOf(data.get("order")));
        intent.putExtra("message", String.valueOf(data.get("message")));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.d(TAG, "message received : " + remoteMessage.getData().toString());
    }

}
