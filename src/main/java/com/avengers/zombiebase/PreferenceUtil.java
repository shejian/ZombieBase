package com.avengers.zombiebase;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author duo.chen
 * @date 2017/4/13
 */
public class PreferenceUtil {

    private static PreferenceUtil preferenceUtil = null;
    private SharedPreferences settings;

    public static final String SETTING_INFO = "PreferencesInfo";

    private PreferenceUtil(Context context) {
        settings = context.getSharedPreferences(SETTING_INFO, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance() {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(BaseApplication.getMaApplication());
        }
        return preferenceUtil;
    }


    public void write(String key, String value) {
        if (key == null) {
            return;
        }
        settings.edit().putString(key, value).apply();
    }

    public void write(String key, boolean value) {
        if (key == null) {
            return;
        }
        settings.edit().putBoolean(key, value).apply();
    }

    public void write(String key, float value) {
        if (key == null) {
            return;
        }
        settings.edit().putFloat(key, value).apply();
    }

    public void write(String key, int value) {
        if (key == null) {
            return;
        }
        settings.edit().putInt(key, value).apply();
    }

    public void write(String key, long value) {
        if (key == null) {
            return;
        }
        settings.edit().putLong(key, value).apply();
    }

    public String readString(String key, String defaultValue) {
        if (key == null) {
            return null;
        }
        return settings.getString(key, defaultValue);
    }

    public int readInt(String key, int defaultValue) {
        if (key == null) {
            return 0;
        }
        return settings.getInt(key, defaultValue);
    }

    public boolean readBoolean(String key, boolean defaultValue) {
        return key != null && settings.getBoolean(key, defaultValue);
    }

}
