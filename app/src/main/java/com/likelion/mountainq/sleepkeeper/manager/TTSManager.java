package com.likelion.mountainq.sleepkeeper.manager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by dnay2 on 2017-04-29.
 */

public class TTSManager {

    private Context mContext;
    private TextToSpeech tts;
    private UtteranceProgressListener progressListener = null;

    public TTSManager(Context mContext) {
        tts = new TextToSpeech(mContext, initListener);
    }

    public void setProgressListener(UtteranceProgressListener progressListener) {
        tts.setOnUtteranceProgressListener(progressListener);
    }

    public void speech(String msg){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID;
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SampleMessage");
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, params, utteranceId);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    public void shutdown(){
        tts.shutdown();
    }

    private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREA);
                tts.setSpeechRate((float) 0.5);
            }
        }
    };

}
