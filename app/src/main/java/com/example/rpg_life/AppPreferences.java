package com.example.rpg_life;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static AppPreferences sInstance;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    private AppPreferences(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences(SavedData.PREFERENCES, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreferences(context);
        }
        return sInstance;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}