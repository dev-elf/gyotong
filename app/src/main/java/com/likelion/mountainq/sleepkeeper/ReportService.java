package com.likelion.mountainq.sleepkeeper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.likelion.mountainq.sleepkeeper.data.Dummy;
import com.likelion.mountainq.sleepkeeper.data.GpsPoint;
import com.likelion.mountainq.sleepkeeper.manager.ConnectionTask;
import com.likelion.mountainq.sleepkeeper.manager.PropertyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by dnay2 on 2017-05-26.
 */

public class ReportService extends Service {
    public static ReportService instance;

    public static ReportService getInstance() {
        return instance;
    }

    private double lat, lon;
    private float accuracy, velocity, yelocity;
    private LocationManager locationManager;
    private int runTime = 0, numMessage=0;

    private ArrayList<GpsPoint> gpsPoints;
    private ArrayList<Float> velocities;
    public SensorTask sensorTask;
    private ConnectionTask connectionTask = new ConnectionTask();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorTask = new SensorTask(getApplicationContext());
        gpsPoints = new ArrayList<>();
        velocities = new ArrayList<>();
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            sensorTask.execute();
            Toast.makeText(getApplicationContext(), "실행되었습니다.",Toast.LENGTH_SHORT).show();
        }catch (SecurityException e){
            e.printStackTrace();
            Toast.makeText(ReportService.this, "권한을 확인해 주세요!", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorTask.quit();
        locationManager.removeUpdates(mLocationListener);
        Toast.makeText(getApplicationContext(), "종료 되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location.getProvider().equals(LocationManager.GPS_PROVIDER)){
                lat = location.getLatitude();
                lon = location.getLongitude();
                accuracy = location.getAccuracy();
                velocity = location.getSpeed();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public interface Listener{
        void goServer();
    }

    public class SensorTask extends AsyncTask<Void, String, Void>{
        long curr = System.currentTimeMillis();
        private static final String TAG = "ConnectionTask";
        private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private boolean flag = true;
        private OkHttpClient client = new OkHttpClient();
        private Context mContext;
        private Notification notification;
        private NotificationCompat.Builder nBuilder;
        private NotificationManager nManager;
        private int notifyID = 1;
        private int vIdx = 0;


        public String json, url;
        public boolean isFlag = false;


        public void setConnection(String url, String json, boolean isFlag){
            this.json = json;
            this.url = url;
            this.isFlag = isFlag;
        }

        private Listener listener;

        public void setListener(Listener listener) {
            this.listener = listener;
        }

        public SensorTask(Context mContext) {
            this.mContext = mContext;
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }



        @Override
        protected Void doInBackground(Void... params) {
            while(flag){
                long time = System.currentTimeMillis();
                if(isFlag){
                    _post(url, json);
                    isFlag = false;
                }

                if( time > curr + 5000){
                    Log.d("service task", "time is running " + runTime);
                    Log.d("service task", "i am in " + lon + " , " + lat);
                    curr = time; // 시간 갱신
                    runTime++; // 5초 단위 추가
                    // 위치값과 속도 갱신
                    gpsPoints.add(new GpsPoint(lat, lon));
                    if(gpsPoints.size() > 1000){
                        gpsPoints.remove(0);
                    }

                    if(runTime % 12 == 0){
                        //분 단위 속도 저장
                        yelocity = Dummy.velocities[vIdx % 16];
                        velocity = Dummy.velocities[++vIdx % 16];
//                        velocities.add(velocity);
                        velocities.add(velocity);
                        if(velocities.size() > 1000){
                            velocities.remove(0);
                        }
                        publishProgress(String.valueOf(velocities.get(velocities.size()-1)) + "m/s" );
                    }
                    boolean flag = true;
                    if(runTime > 100 || flag){

                        /*
                         * 60km/h(16.8 m/s) 로 달리다가 급정지
                         * 10km/h(2.8 m/s) 로 되어서 문제 발생
                         */
                        Log.d("task", "velocity : " + velocity + " && velocity submit : " + (yelocity - velocity));
                        if(velocity < 2.8f && velocities.size() > 0 && yelocity- velocity > 10.0f){
                            String URL = "https://gyotong-jomno.c9users.io/home/request";
                            JSONObject jsonObject = new JSONObject();
                            try{
                                jsonObject.put("token", PropertyManager.getInstance().getPushToken());
                                jsonObject.put("latitude", lat);
                                jsonObject.put("longitude", lon);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            String json = jsonObject.toString();
                            _post(URL, json);
                            publishProgress(ConnectionTask.POST);
                        }
                    }


                }
            }
            return null;
        }



        public void quit(){
            flag = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nBuilder = buildCustomNotification("아직 파악 중입니다.");
            notification = nBuilder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            nManager.notify(notifyID, notification);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nManager.cancel(notifyID);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals(ConnectionTask.POST)){
                Toast.makeText(mContext, "뭔가 발생했다!", Toast.LENGTH_SHORT).show();
            }
            else
                updateNotification(values[0]);
        }

        public String _post(String url, String json){
            try{
                return postMethod(url, json);
            }catch (IOException e){
                return e.getMessage();
            }
        }
        private String postMethod (String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .header("content-type", "application/json")
                    .post(body)
                    .build();
            Log.d(TAG, "post request : " + request.body().toString());
            okhttp3.Response response = client.newCall(request).execute();
            return response.body().string();
        }


        private void updateNotification(String message){
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_report_noti);
            remoteViews.setTextViewText(R.id.nowSpeed, message);
            nBuilder.setContent(remoteViews)
                    .setNumber(++numMessage);
            notification = nBuilder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            nManager.notify(notifyID, notification);
        }

        private NotificationCompat.Builder buildSimpleNotification(String code, String idx, String title, String message) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setColor(getColor(R.color.colorAccent))
                    .setContentIntent(getPendingIntent());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setColor(getColor(R.color.colorAccent))
                        .setSmallIcon(R.mipmap.ic_launcher);

            }
            return builder;
        }

        private NotificationCompat.Builder buildCustomNotification(String message) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_report_noti);
            remoteViews.setImageViewResource(R.id.logoImg, R.mipmap.ic_launcher);
            remoteViews.setTextViewText(R.id.nowSpeed, message);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                    .setContent(remoteViews)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(false)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setNumber(numMessage)
                    .setContentIntent(getPendingIntent())
                    .setDefaults(Notification.DEFAULT_ALL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);
            }

            return notificationBuilder;
        }


        private PendingIntent getPendingIntent() {
            Intent intent = null;

            intent = new Intent(mContext, ReportActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(ReportActivity.class);
            stackBuilder.addNextIntent(intent);

            return stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
    }


}
