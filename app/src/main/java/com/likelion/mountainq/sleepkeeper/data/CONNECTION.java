package com.likelion.mountainq.sleepkeeper.data;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by dnay2 on 2017-05-28.
 */

public class CONNECTION{
    private static String result = "";
    public static String post(final String url, final String json){
        new Thread(new Runnable() {
            @Override
            public void run() {
                result = _post(url, json);
            }
        });
        return result;
    }


    private static final String TAG = "ConnectionTask";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static final String POST = "post";

    private static String _get(String url){
        try{
            return getMethod(url);
        }catch (IOException e){
            return e.getMessage();
        }
    }

    private static String _put(String url, Map<String, String> header, String json){
        try{
            return putMethod(url, header, json);
        }catch (IOException e){
            return e.getMessage();
        }
    }
    private static String _post(String url, String json){
        try{
            return postMethod(url, json);
        }catch (IOException e){
            return e.getMessage();
        }
    }
    private static String postMethod (String url, String json) throws IOException {
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

    private static String getMethod (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String putMethod (String url, Map<String, String> headers, String jsonBody) throws IOException{
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
