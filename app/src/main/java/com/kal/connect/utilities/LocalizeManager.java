package com.kal.connect.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

//import com.sultanburger.deliveryboy.utilities.AppPreferences;

public class LocalizeManager {

    // To refresh the activities in all the classes
    public static Boolean refreshActivities = false;

    public static Context setLocale(Context c) {

        return updateResources(c, getLanguage());

    }

    public static Context setNewLocale(Context c, String language) {

        refreshActivities = true;
        saveLanguage(language);
        return updateResources(c, language);

    }

    public static String getLanguage() {

        return AppPreferences.getInstance().retrieveLanguage();

    }

    private static void saveLanguage(String language) {

//        AppPreferences.getInstance().setLanguagePreferences(language);
        AppPreferences.getInstance().setPreferences(AppPreferences.LANGUAGE,language );

    }

    private static Context updateResources(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
            context.getResources().updateConfiguration(config, res.getDisplayMetrics());
        }
        else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }

        return context;
    }

    public static Locale getCurrentAppLocale(Resources res) {

        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;

    }
}
