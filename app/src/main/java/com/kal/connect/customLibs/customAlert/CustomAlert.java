package com.kal.connect.customLibs.customAlert;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.kal.connect.R;
import com.kal.connect.utilities.Config;
import com.tapadoo.alerter.Alerter;
/**
 * Created by lakeba_prabhu on 04/03/17.
 */

public class CustomAlert {

    static int dismissTime = 2000;
    static String failedColor = "#049294";
    static String successColor = "#049294";

    public CustomAlert() {

    }

    /**
     * Build Alert with the required params
     * @param context
     * @param message
     * @param status
     */
    public static void showAlert(Context context, String message, Boolean status) {

        String alertColor = (status)? successColor : failedColor;
        int alertIcon = (status)? R.drawable.icon_validation_succeed : R.drawable.icon_validation_failed;
        Alerter.create((Activity)(context)).setTitle(Config.AppName).setText(message).setIcon(alertIcon).setBackgroundColorInt(Color.parseColor(alertColor)).setDuration(dismissTime).show();

    }

}
