package com.kal.connect.customLibs.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kal.connect.R;
import com.kal.connect.modules.communicate.ChatActivity;
import com.kal.connect.modules.communicate.IncomingCallActivity;
import com.kal.connect.appconstants.OpenTokConfigConstants;
import com.kal.connect.modules.communicate.services.HeadsUpNotificationService;
import com.kal.connect.modules.dashboard.DashboardMapActivity;
import com.kal.connect.utilities.AppPreferences;
import com.kal.connect.utilities.Config;
import com.kal.connect.utilities.SplashActivity;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Map<String, String> messageData = message.getData();
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();

        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        if (messageData.containsKey("title") && message.getData().get("title").toString().contains("Video Call")) {
            if (messageData.containsKey("sessionID") && messageData.containsKey("tokenID")) {
                OpenTokConfigConstants.SESSION_ID = message.getData().get("sessionID");
                OpenTokConfigConstants.TOKEN = message.getData().get("tokenID");

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    if (!isScreenOn) {
                        sendNotification(this, "", message.getData().get("title").toString(), message.getData().get("SpecialistID").toString());
                        return;
                    } else {
                        if (!isInBackground) {
                            Intent fullScreenIntent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                            if (message.getData().containsKey("SpecialistID")) {
                                fullScreenIntent.putExtra("SpecialistID", message.getData().get("SpecialistID").toString());
                            }
                            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(fullScreenIntent);
                            return;
                        } else {
                            Intent headsup = new Intent(this, HeadsUpNotificationService.class);
                            headsup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            headsup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (message.getData().containsKey("SpecialistID")) {
                                headsup.putExtra("SpecialistID", message.getData().get("SpecialistID").toString());
                            }
                            headsup.putExtra("CALER_NAME", message.getData().get("body").toString());
                            headsup.putExtra("CALL_TYPE", 1);
                            startService(headsup);
                            return;
                        }
                    }
                } else {
                    if (!isScreenOn) {
                        if (message.getData().containsKey("SpecialistID")) {
                            sendNotification(this, "", message.getData().get("title").toString(), message.getData().get("SpecialistID").toString());
                        } else {
                            sendNotification(this, "", message.getData().get("title").toString(), "");
                        }
                        return;
                    } else {
                        if (!isInBackground) {
                            Intent fullScreenIntent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                            if (message.getData().containsKey("SpecialistID")) {
                                fullScreenIntent.putExtra("SpecialistID", message.getData().get("SpecialistID").toString());
                            }
                            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(fullScreenIntent);
                            return;
                        } else {
                            Intent headsup = new Intent(this, HeadsUpNotificationService.class);
                            headsup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            headsup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (message.getData().containsKey("SpecialistID")) {
                                headsup.putExtra("SpecialistID", message.getData().get("SpecialistID").toString());
                            }
                            headsup.putExtra("CALER_NAME", message.getData().get("body").toString());
                            headsup.putExtra("CALL_TYPE", 1);
                            startService(headsup);
                            return;
                        }
                    }

                }
            }
        } else if (messageData.containsKey("title") && message.getData().get("title") != null && message.getData().get("title").toString().toLowerCase().contains("appointment") && messageData.containsKey("Notificationmessage")) {
            prepareNotificationForRemainder(message.getData().get("Notificationmessage").toString());
        } else if (messageData.containsKey("title") && message.getData().get("title").equalsIgnoreCase("Chat")) {

            OpenTokConfigConstants.SESSION_ID = message.getData().get("sessionID");
            OpenTokConfigConstants.TOKEN = message.getData().get("tokenID");
            Config.isBack = true;
            if (!isInBackground) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), ChatActivity.class);
                fullScreenIntent.putExtra("doctorName", message.getData().get("body"));
                fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(fullScreenIntent);
            } else {
                sendNotification(this, message.getData().get("body"), message.getData().get("title").toString(), "");
            }
        } else if (messageData.containsKey("title") && message.getData().get("title").toString().equalsIgnoreCase("Decline Call")) {
            try {
                if (Config.mActivity != null && !Config.mActivity.getClass().getSimpleName().equalsIgnoreCase("VideoConference")) {
                    ((IncomingCallActivity) Config.mActivity).moveToHome();
                } else {
                    Intent homeScreen = new Intent(getApplicationContext(), DashboardMapActivity.class);
                    homeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeScreen);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(final Context context, String body, String type, String specialistID) {

        if (!AppPreferences.getInstance().getIsAppKilled()) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent fullScreenIntent = null;
            if (type != null && !type.equalsIgnoreCase("Chat")) {
                type = "VideoCall";
                fullScreenIntent = new Intent(context, IncomingCallActivity.class);
                fullScreenIntent.putExtra("SpecialistID", specialistID);
            } else {
                fullScreenIntent = new Intent(context, ChatActivity.class);
                fullScreenIntent.putExtra("doctorName", body);
            }
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context, type)
                            .setSmallIcon(R.mipmap.ayurvedha)
                            .setContentTitle(type)
                            .setSubText(body)
                            .setShowWhen(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_ALARM)
                            .setAutoCancel(true)
                            .setSound(alarmTone)
//                            .setTimeoutAfter(10000)
                            .setFullScreenIntent(fullScreenPendingIntent, true).setAutoCancel(true);
            long[] v = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400};
            notificationBuilder.setVibrate(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(Color.RED);
                notificationBuilder.setSmallIcon(R.mipmap.ayurvedha);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(type, type, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationBuilder.setChannelId(type);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(987, notificationBuilder.build());
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