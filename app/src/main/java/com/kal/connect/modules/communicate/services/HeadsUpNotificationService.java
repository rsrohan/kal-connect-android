package com.kal.connect.modules.communicate.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kal.connect.R;
import com.kal.connect.appconstants.CallFeatureConstant;
import com.kal.connect.utilities.Config;

import java.util.Objects;

public class HeadsUpNotificationService extends Service {
    private static final String TAG = "HeadsUpNotification";
    private String CHANNEL_ID = "VoipChannel";
    private String CHANNEL_NAME = "Voip Channel";
    public static AudioPlayer mAudioPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle data = null;


        try {
            Intent receiveCallAction = new Intent(getApplicationContext(), HeadsUpNotificationActionReceiver.class);

            receiveCallAction.putExtra(CallFeatureConstant.CALL_RESPONSE_ACTION_KEY, CallFeatureConstant.CALL_RECEIVE_ACTION);
//            receiveCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            receiveCallAction.setAction("RECEIVE_CALL");

            Intent cancelCallAction = new Intent(getApplicationContext(), HeadsUpNotificationActionReceiver.class);
            cancelCallAction.putExtra(CallFeatureConstant.CALL_RESPONSE_ACTION_KEY, CallFeatureConstant.CALL_CANCEL_ACTION);
//            cancelCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            cancelCallAction.setAction("CANCEL_CALL");

            PendingIntent receiveCallPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1200, receiveCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent cancelCallPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1201, cancelCallAction, PendingIntent.FLAG_UPDATE_CURRENT);

            createChannel();
            NotificationCompat.Builder notificationBuilder = null;

            mAudioPlayer = AudioPlayer.getInstance();
            mAudioPlayer.playRingtone(getApplicationContext());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        mAudioPlayer.stopRingtone();
                    }catch (Exception e){
                        Log.e(TAG, "run: "+e );
                    }
                }
            }, 60000);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentText(intent.getStringExtra("remoteUserName"))
                    .setContentTitle("Incoming Video Call")
                    .setSmallIcon(R.drawable.call_icon)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .addAction(R.drawable.call_icon, "Receive Call", receiveCallPendingIntent)
                    .addAction(R.drawable.call_disconnect, "Cancel Call", cancelCallPendingIntent)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setTimeoutAfter(60000)
                    .setFullScreenIntent(receiveCallPendingIntent, true)
                    .setAutoCancel(true);
            notificationBuilder.setOngoing(true);


            Notification incomingCallNotification = null;
            if (notificationBuilder != null) {
                incomingCallNotification = notificationBuilder.build();
//            startForeground(120, incomingCallNotification);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    /*
    Create notification channel if OS version is greater than or equal to Oreo
    */
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Notifications");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            Objects.requireNonNull(getApplicationContext().getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }

}


