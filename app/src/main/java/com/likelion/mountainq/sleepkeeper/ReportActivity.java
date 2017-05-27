package com.likelion.mountainq.sleepkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.likelion.mountainq.sleepkeeper.manager.PropertyManager;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private static final int APP_PERMISSIONS = 100;

    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //권한이 없음
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                // 권한 취소 이력이 있는 경우
                ActivityCompat.requestPermissions(ReportActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, APP_PERMISSIONS);
            }else {
                // 권한을 처음 요청하는 경우
                ActivityCompat.requestPermissions(ReportActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, APP_PERMISSIONS);
            }
        }else {
            //권한이 있는 경우
            Log.d(TAG, "permission checked ");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case APP_PERMISSIONS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "get permissions");
                } else {
                    Log.d(TAG, "fail permissions program is quited");
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if(PropertyManager.getInstance().getPushToken().equals("null")){
            String token = FirebaseInstanceId.getInstance().getToken();
            PropertyManager.getInstance().setPushToken(token);
            Log.d(TAG, "refreshed token : " + token);
        }
        Log.d(TAG, "token : " + PropertyManager.getInstance().getPushToken());

        checkPermissions();

        // AdMob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3799982169205796~7154680463");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // adMob end

    }

    public void btnService (View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.serviceStart:
                intent = new Intent(ReportActivity.this, ReportService.class);
                intent.putExtra("count", "1");
                startService(intent);
                break;
            case R.id.serviceFinish:
                intent = new Intent(ReportActivity.this, ReportService.class);
                stopService(intent);
                break;
        }
    }
}
