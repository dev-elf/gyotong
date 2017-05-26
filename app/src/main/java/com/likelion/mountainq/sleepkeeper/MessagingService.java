package com.likelion.mountainq.sleepkeeper;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(MessagingService.this, Pop.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.d(TAG, "message received : " + remoteMessage.getData().toString());
    }

}
