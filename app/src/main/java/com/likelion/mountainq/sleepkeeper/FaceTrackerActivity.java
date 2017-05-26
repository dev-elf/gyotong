package com.likelion.mountainq.sleepkeeper;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.likelion.mountainq.sleepkeeper.helper.ApplicationController;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.InputStream;

/**
 * Created by dnay2 on 2017-05-04.
 */

public class FaceTrackerActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_IMAGE = 0;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private ProgressDialog mProgressDialog;

    private class DetectionTask extends AsyncTask<InputStream, String, Face[]>{
        private boolean mSucceed = true;

        @Override
        protected Face[] doInBackground(InputStream... params) {
            FaceServiceClient faceServiceClient = ApplicationController.getFaceServiceClient();
            try{
                publishProgress("Detecting..");

                //Start detection
                return faceServiceClient.detect(
                        params[0],
                        true,
                        true,
                        new FaceServiceClient.FaceAttributeType[]{
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender,
                                FaceServiceClient.FaceAttributeType.Smile,
                                FaceServiceClient.FaceAttributeType.Glasses,
                                FaceServiceClient.FaceAttributeType.FacialHair,
                                FaceServiceClient.FaceAttributeType.Emotion,
                                FaceServiceClient.FaceAttributeType.HeadPose
                        });
            }catch (Exception e){
                mSucceed = false;
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.app_name));

    }
}
