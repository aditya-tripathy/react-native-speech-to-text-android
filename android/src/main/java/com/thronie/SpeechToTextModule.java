package com.thronie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
// import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.ArrayList;
import java.util.Locale;

import com.thronie.ErrorConstants;

// import static android.content.ContentValues.TAG;

public class SpeechToTextModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Promise speechTextPromise;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        //listener for activity
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (speechTextPromise != null) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ArrayList < String > result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        speechTextPromise.resolve(result.get(0));
                        speechTextPromise = null;
                        break;
                    case Activity.RESULT_CANCELED:
                        speechTextPromise.reject("Failed", ErrorConstants.E_VOICE_CANCELLED);
                        speechTextPromise = null;
                        break;
                    case RecognizerIntent.RESULT_AUDIO_ERROR:
                        speechTextPromise.reject("Failed", ErrorConstants.E_AUDIO_ERROR);
                        speechTextPromise = null;
                        break;
                    case RecognizerIntent.RESULT_NETWORK_ERROR:
                        speechTextPromise.reject("Failed", ErrorConstants.E_NETWORK_ERROR);
                        speechTextPromise = null;
                        break;
                    case RecognizerIntent.RESULT_NO_MATCH:
                        speechTextPromise.reject("Failed", ErrorConstants.E_NO_MATCH);
                        speechTextPromise = null;
                        break;
                    case RecognizerIntent.RESULT_SERVER_ERROR:
                        speechTextPromise.reject("Failed", ErrorConstants.E_SERVER_ERROR);
                        speechTextPromise = null;
                        break;
                }
            }
        }
    };

    public SpeechToTextModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "SpeechToText";
    }

    @ReactMethod
    public void startSpeech(String prompt, String locale, final Promise promise) {
        // TODO: Implement some actually useful functionality
        // Log.d(TAG, "startSpeech called ");
        speechTextPromise = promise;
        if (this.reactContext.getCurrentActivity() == null) {
            speechTextPromise.reject("Failed", "Activity doesn't exist");
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLocale(locale));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getPrompt(prompt));
        try {
            this.reactContext.startActivityForResult(intent, 5834, null);
        } catch (Exception e) {
            speechTextPromise.reject("Failed", ErrorConstants.E_FAILED_TO_SHOW_VOICE);
            speechTextPromise = null;
        }
    }


    private String getPrompt(String prompt) {
        if (prompt != null && !prompt.equals("")) {
            return prompt;
        }
        return "Say something";
    }

    private String getLocale(String locale) {
        if (locale != null && !locale.equals("")) {
            return locale;
        }
        return Locale.getDefault().toString();
    }
}