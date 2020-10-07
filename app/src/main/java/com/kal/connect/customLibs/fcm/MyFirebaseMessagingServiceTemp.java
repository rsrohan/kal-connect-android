package com.kal.connect.customLibs.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kal.connect.R;
import com.kal.connect.modules.communicate.MyInCallService;
import com.kal.connect.modules.communicate.OpenTokConfig;
import com.kal.connect.modules.communicate.services.HeadsUpNotificationService;
import com.kal.connect.modules.dashboard.Dashboard;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Splash;

import java.util.Map;


public class MyFirebaseMessagingServiceTemp extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
//        sendMyNotification(message.getNotification().getBody());


        Map<String, String> messageData = message.getData();
        if (messageData.containsKey("title") && message.getData().get("title").toString().toLowerCase().contains("video")) {
            if (messageData.containsKey("sessionID") && messageData.containsKey("tokenID")) {
//                Intent intent = new Intent(this, IncommingCall.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("SESSION_ID",message.getData().get("sessionID").toString());
//                intent.putExtra("TOKEN",message.getData().get("tokenID").toString());

                OpenTokConfig.SESSION_ID = message.getData().get("sessionID");
                OpenTokConfig.TOKEN = message.getData().get("tokenID");
//                intent.putExtra("CALER_NAME",message.getData().get("body").toString());
//                intent.putExtra("CALL_TYPE",1);

                Intent myInCallService = new Intent(this, MyInCallService.class);
                myInCallService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myInCallService.putExtra("CALER_NAME", message.getData().get("body").toString());
                myInCallService.putExtra("CALL_TYPE", 1);


                try {
                    //Issue here, this below line not happening sometime because of we calling from Background service.
//                    startActivity(intent);

                    ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                    ActivityManager.getMyMemoryState(myProcess);

                    Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

                    if (!isInBackground) {
                        startService(myInCallService);
                        return;
                    } else {

                    }


                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || !isInBackground) {
                        startService(myInCallService);
                        return;
                    } else {
                        //For Android 10 when app is in background
                        Intent headsup = new Intent(this, HeadsUpNotificationService.class);
                        headsup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        headsup.putExtra("CALER_NAME", message.getData().get("body").toString());
                        headsup.putExtra("CALL_TYPE", 1);
                        startService(headsup);
                        return;
                    }


                } catch (Exception e) {
                    Log.e("OnMessage ", "onMessageReceived :: ::: " + e.getMessage());
                }

            }
        } else if (messageData.containsKey("title") && message.getData().get("title").toString().toLowerCase().contains("appointment") && messageData.containsKey("Notificationmessage")) {
            prepareNotificationForRemainder(message.getData().get("Notificationmessage").toString());
        }
    }


    private void sendMyNotification(String message) {

        //On click of notification it redirect to this Activity
//        Intent intent = new Intent(this, CallerUI.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(intent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("My Firebase Push notification")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());

    }

    private void prepareNotificationForRemainder(String message) {
        createNotificationChannel();
        Intent intent = new Intent(this, Splash.class);

        if (AppPreferences.getInstance().checkLogin(getApplicationContext())) {
            intent = new Intent(this, Dashboard.class);
        }


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("FromNotification", true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Remainder-ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(123, builder.build());

//        manager.notify(123, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Remainder-ID";
            String description = "Remainder for Appointment";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Remainder-ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}