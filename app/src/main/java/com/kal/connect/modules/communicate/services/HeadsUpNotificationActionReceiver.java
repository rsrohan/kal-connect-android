package com.kal.connect.modules.communicate.services;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.kal.connect.appconstants.CallFeatureConstant;
import com.kal.connect.modules.communicate.VideoConferenceActivity;
import com.kal.connect.utilities.Config;

import static com.kal.connect.modules.communicate.services.HeadsUpNotificationService.mAudioPlayer;


public class HeadsUpNotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Ringtone r = RingtoneManager.getRingtone(context, defaultSoundUri);

            r.stop();
        }catch (Exception e){}
        try {
            if (intent != null && intent.getExtras() != null) {
                String action = intent.getStringExtra(CallFeatureConstant.CALL_RESPONSE_ACTION_KEY);
    //            Bundle data = intent.getBundleExtra(ConstantApp.FCM_DATA_KEY);

                if (action != null) {
                    performClickAction(context, action);
                }

                // Close the notification after the click action is performed.

                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
                context.stopService(new Intent(context, HeadsUpNotificationService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performClickAction(Context context, String action) {

        try {
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);

            mAudioPlayer.stopRingtone();

            if (action.equals(CallFeatureConstant.CALL_RECEIVE_ACTION)) {
                Intent videoIntent = null;
                try {
                    videoIntent = new Intent(context, VideoConferenceActivity.class);
                    videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                    if(Config.ringtone!=null){
//                        Config.ringtone.stop();
//                    }

    //                if(isTaskRoot()){
    //                    videoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //                }

                    videoIntent.putExtra("CALL_TYPE",1);
                    context.startActivity(videoIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (action.equals(CallFeatureConstant.CALL_CANCEL_ACTION)) {
//                if(Config.ringtone!=null){
//                    Config.ringtone.stop();
//                }
                context.stopService(new Intent(context, HeadsUpNotificationService.class));
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (action.equals(ConstantApp.CALL_RECEIVE_ACTION) && data != null && data.get("type").equals("voip")) {
//            Intent openIntent = null;
//            try {
//                openIntent = new Intent(context, VoiceCallActivity.class"));
//                        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(openIntent);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else if (action.equals(ConstantApp.CALL_RECEIVE_ACTION) && data != null && data.get("type").equals("video")) {
//            Intent openIntent = null;
//            try {
//                openIntent = new Intent(context, VideoCallActivity.class"));
//                        openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(openIntent);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else if (action.equals(ConstantApp.CALL_CANCEL_ACTION)) {
//            context.stopService(new Intent(context, HeadsUpNotificationService.class));
//            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            context.sendBroadcast(it);
//        }
    }
}