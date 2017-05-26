package com.likelion.mountainq.sleepkeeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class Pop extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));

    }

    private static final int ACCIDENT = 100;
    private static final int BROKENCAR = 200;
    private static final int NONE = 300;
    private static final String URL = "http://";

    public void btnReport(View v){

//        ConnectionTask task = new ConnectionTask();
//        JSONObject jsonObject = new JSONObject();
//        try{
//            switch (v.getId()){
//                case R.id.accident:
//                    jsonObject.put("status", ACCIDENT);
//                    break;
//                case R.id.brokencar:
//                    jsonObject.put("status", BROKENCAR);
//                    break;
//                case R.id.none:
//                    jsonObject.put("status", NONE);
//                    break;
//            }
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String message = jsonObject.toString();
//        task._post(URL, message);
//
        switch (v.getId()){
            case R.id.accident:
                Toast.makeText(Pop.this, "사고 발생", Toast.LENGTH_SHORT).show();
                break;
            case R.id.brokencar:
                Toast.makeText(Pop.this, "차량 결함 발생", Toast.LENGTH_SHORT).show();
                break;
            case R.id.none:
                Toast.makeText(Pop.this, "아무일도 없음 앗 나의 실수", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
