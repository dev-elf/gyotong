package com.likelion.mountainq.sleepkeeper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.likelion.mountainq.sleepkeeper.manager.ConnectionTask;
import com.likelion.mountainq.sleepkeeper.manager.TTSManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class Pop extends AppCompatActivity {

    private static final String TAG = "POP";
    private static final int REPORT = 100;
    private static final int ALARM = 200;
    private static final int IGNORE = 300;

    private TTSManager ttsManager;
    private String message;
    private ConnectionTask connectionTask = new ConnectionTask();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int order = intent.getIntExtra("order", 300);
        switch (order){
            case REPORT:
                setContentView(R.layout.activity_report_pop);
                break;
            case ALARM:

                setContentView(R.layout.activity_report_alarm);
                message = intent.getStringExtra("message");
                if(message != null && !message.equals("")){
                    message = "이천십칠년 공오월 이십팔일 오전 세시경 경부고속도로 화성휴게소 인근 하행 도로에서 2중추돌 사고가 발생하였습니다.";
                }
                Log.d(TAG, "order : " + order + "  message : " + message);
                ((TextView) findViewById(R.id.message)).setText(message);
                ttsManager = new TTSManager(getApplicationContext());
//                ttsManager.speech(message + " 안전운전 하십시오");

                break;
            case IGNORE:
                finish();
                break;
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));

        if(order == ALARM){
            new EXITTask().execute();
        }

    }

    private class EXITTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            long time = System.currentTimeMillis();
            while(time + 10000 > System.currentTimeMillis()){
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            ttsManager.speech(message);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }



    private static final int ACCIDENT = 100;
    private static final int BROKENCAR = 200;
    private static final int NONE = 300;
    private static final String URL = "https://gyotong-jomno.c9users.io/home/report";

    public void btnReport(View v){

        JSONObject jsonObject = new JSONObject();
        try{
            switch (v.getId()){
                case R.id.accident:
                    jsonObject.put("status", ACCIDENT);
                    break;
                case R.id.brokencar:
                    jsonObject.put("status", BROKENCAR);
                    break;
                case R.id.none:
                    jsonObject.put("status", NONE);
                    break;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        String message = jsonObject.toString();
        ReportService service = ReportService.getInstance();
        if(service != null){
            service.sensorTask.setConnection(URL, message, true);
        } else {
            new ConnectionTask().execute(ConnectionTask.POST, URL, message);
        }
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
        finish();
    }
}
