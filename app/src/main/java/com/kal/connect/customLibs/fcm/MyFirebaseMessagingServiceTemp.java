package com.kal.connect.customLibs.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kal.connect.R;
import com.kal.connect.modules.communicate.services.MyInCallService;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.modules.communicate.services.HeadsUpNotificationService;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.SplashActivity;

import java.util.Map;


public class MyFirebaseMessagingServiceTemp extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Map<String, String> messageData = message.getData();
        if (messageData.containsKey("title") && message.getData().get("title").toString().toLowerCase().contains("video")) {
            if (messageData.containsKey("sessionID") && messageData.containsKey("tokenID")) {

                OpenTokConfigConstants.SESSION_ID = message.getData().get("sessionID");
                OpenTokConfigConstants.TOKEN = message.getData().get("tokenID");


                Intent myInCallService = new Intent(this, MyInCallService.class);
                myInCallService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myInCallService.putExtra("CALER_NAME", message.getData().get("body").toString());
                myInCallService.putExtra("CALL_TYPE", 1);


                try {

                    ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                    ActivityManager.getMyMemoryState(myProcess);

                    boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

                    if (!isInBackground) {
                        startService(myInCallService);
                        return;
                    } else {

                    }


                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || !isInBackground) {
                        startService(myInCallService);
                    } else {
                        //For Android 10 when app is in background
                        Intent headsup = new Intent(this, HeadsUpNotificationService.class);
                        headsup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        headsup.putExtra("CALER_NAME", message.getData().get("body").toString());
                        headsup.putExtra("CALL_TYPE", 1);
                        startService(headsup);
                    }
                    return;


                } catch (Exception e) {
                    Log.e("OnMessage ", "onMessageReceived :: ::: " + e.getMessage());
                }

            }
        } else if (messageData.containsKey("title") && message.getData().get("title").toString().toLowerCase().contains("appointment") && messageData.containsKey("Notificationmessage")) {
            prepareNotificationForRemainder(message.getData().get("Notificationmessage").toString());
        }
    }


    private void prepareNotificationForRemainder(String message) {
        createNotificationChannel();
        Intent intent = new Intent(this, SplashActivity.class);

        if (AppPreferences.getInstance().checkLogin(getApplicationContext())) {
            intent = new Intent(this, DashboardMapActivity.class);
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

}