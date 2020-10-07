package com.kal.connect.modules.communicate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.app.Service;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyInCallService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle mBundle =intent.getExtras();


        Intent incommingCallIntent = new Intent(this, IncommingCall.class);
//        incommingCallIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        incommingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        incommingCallIntent.putExtra("CALER_NAME",mBundle.getString("CALER_NAME"));
        incommingCallIntent.putExtra("CALL_TYPE",String.valueOf(mBundle.getInt("CALL_TYPE")));
        startActivity(incommingCallIntent);
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
