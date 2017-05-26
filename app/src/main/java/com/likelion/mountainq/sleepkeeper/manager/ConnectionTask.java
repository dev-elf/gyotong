package com.likelion.mountainq.sleepkeeper.manager;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * created by geusan
 */

public class ConnectionTask extends AsyncTask<Integer, Integer, Integer> {

    private static final String TAG = "ConnectionTask";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public ConnectionTask() {
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        return null;
    }

    public String _post(String url, String json){
        try{
            return postMethod(url, json);
        }catch (IOException e){
            return e.getMessage();
        }
    }

    public String _get(String url){
        try{
            return getMethod(url);
        }catch (IOException e){
            return e.getMessage();
        }
    }

    public String _put(String url, Map<String, String> header, String json){
        try{
            return putMethod(url, header, json);
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

    private String getMethod (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String putMethod (String url, Map<String, String> headers, String jsonBody) throws IOException{
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Log.d(TAG, "put request ");
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .put(body)
                .build();

        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
