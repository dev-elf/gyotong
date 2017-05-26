package com.likelion.mountainq.sleepkeeper.helper;

import android.app.Application;

import com.likelion.mountainq.sleepkeeper.R;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

/**
 * Created by dnay2 on 2017-05-10.
 */

public class ApplicationController extends Application {

    private static ApplicationController instance;

    public static ApplicationController getInstance() {
        return instance;
    }

    private static FaceServiceClient sFaceServiceClient;

    public static FaceServiceClient getFaceServiceClient(){
        return sFaceServiceClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sFaceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));
    }
}
