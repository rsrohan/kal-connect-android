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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kal.connect.R;
import com.kal.connect.appconstants.ConstantApp;
import com.kal.connect.utilities.Config;

import java.util.Objects;

public class HeadsUpNotificationService extends Service {
    private String CHANNEL_ID = "VoipChannel";
    private String CHANNEL_NAME = "Voip Channel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle data = null;


        if (intent != null && intent.getExtras() != null) {
//            data = intent.getBundleExtra("");
        }
        try {
            Intent receiveCallAction = new Intent(getApplicationContext(), HeadsUpNotificationActionReceiver.class);

            receiveCallAction.putExtra(ConstantApp.CALL_RESPONSE_ACTION_KEY, ConstantApp.CALL_RECEIVE_ACTION);
//            receiveCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            receiveCallAction.setAction("RECEIVE_CALL");

            Intent cancelCallAction = new Intent(getApplicationContext(), HeadsUpNotificationActionReceiver.class);
            cancelCallAction.putExtra(ConstantApp.CALL_RESPONSE_ACTION_KEY, ConstantApp.CALL_CANCEL_ACTION);
//            cancelCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            cancelCallAction.setAction("CANCEL_CALL");

            PendingIntent receiveCallPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1200, receiveCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent cancelCallPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1201, cancelCallAction, PendingIntent.FLAG_UPDATE_CURRENT);

            createChannel();
            NotificationCompat.Builder notificationBuilder = null;
//            if (data != null) {
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setContentText(intent.getStringExtra("remoteUserName"))
//                        .setContentTitle("Incoming Video Call")
//                        .setSmallIcon(R.drawable.call_icon)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setCategory(NotificationCompat.CATEGORY_CALL)
//                        .addAction(R.drawable.call_icon, "Receive Call", receiveCallPendingIntent)
//                        .addAction(R.drawable.call_disconnect, "Cancel call", cancelCallPendingIntent)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setFullScreenIntent(receiveCallPendingIntent, true);
//            }

            playRingTone();

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentText(intent.getStringExtra("remoteUserName"))
                    .setContentTitle("Incoming Video Call")
                    .setSmallIcon(R.drawable.call_icon)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .addAction(R.drawable.call_icon, "Receive Call", receiveCallPendingIntent)
                    .addAction(R.drawable.call_disconnect, "Cancel call", cancelCallPendingIntent)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setTimeoutAfter(20000)
                    .setFullScreenIntent(receiveCallPendingIntent, true).setAutoCancel(true);

//            NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
//                    .setSmallIcon(Util.getNotificationIcon(context))
//                    .setContentTitle(new SessionManager().getDomainPreference(context))
//                    .setAutoCancel(true)
//                    .setOngoing(false)
//                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setShowWhen(false)
//                    .setContentText(summaryText)
//                    .setTimeoutAfter(3000) // add time in milliseconds
//                    .setChannelId(CHANNEL_ID);

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
    Create noticiation channel if OS version is greater than or eqaul to Oreo
    */
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Notifications");
//            Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
//
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();

//            channel.setSound(defaultRintoneUri,audioAttributes);

            Objects.requireNonNull(getApplicationContext().getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }

    void playRingTone() {
        try {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Config.ringtone = RingtoneManager.getRingtone(this, uri);
            Config.ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Config.ringtone != null) {
                    Config.ringtone.stop();
                }
            }
        }, 10000);

    }
}


