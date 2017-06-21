package com.extralarge.fujitsu.xl.FCM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.extralarge.fujitsu.xl.ReporterSection.Verifyotp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";
    static String refreshedToken;

    @Override
    public void  onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("NameOftoken", refreshedToken);
//        editor.commit();

        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        TokenSave.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    public static String sendtokentoactivity(){
        return refreshedToken;
    }
}
