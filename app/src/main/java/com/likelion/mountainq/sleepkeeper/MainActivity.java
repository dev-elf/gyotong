package com.likelion.mountainq.sleepkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.likelion.mountainq.sleepkeeper.ui.DetectionActivity;
import com.likelion.mountainq.sleepkeeper.ui.FindSimilarFaceActivity;
import com.likelion.mountainq.sleepkeeper.ui.GroupingActivity;
import com.likelion.mountainq.sleepkeeper.ui.IdentificationActivity;
import com.likelion.mountainq.sleepkeeper.ui.VerificationMenuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }
    }

    public void detection(View view) {
        Intent intent = new Intent(this, DetectionActivity.class);
        startActivity(intent);
    }

    public void verification(View view) {
        Intent intent = new Intent(this, VerificationMenuActivity.class);
        startActivity(intent);
    }

    public void grouping(View view) {
        Intent intent = new Intent(this, GroupingActivity.class);
        startActivity(intent);
    }

    public void findSimilarFace(View view) {
        Intent intent = new Intent(this, FindSimilarFaceActivity.class);
        startActivity(intent);
    }

    public void identification(View view) {
        Intent intent = new Intent(this, IdentificationActivity.class);
        startActivity(intent);
    }
}
