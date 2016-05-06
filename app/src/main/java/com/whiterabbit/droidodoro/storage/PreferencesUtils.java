package com.whiterabbit.droidodoro.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fedepaol on 07/05/16.
 */
public class PreferencesUtils {
    private static final String PREFS_NAME = "com.whiterabbit.preferences";
    private static final String TOKEN_ID = "com.whiterabbit.token";

    private SharedPreferences mPreferences;

    public PreferencesUtils(Context c) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(TOKEN_ID, token);
        editor.apply();
    }

    public String getAuthToken() {
        return mPreferences.getString(TOKEN_ID, "");
    }
}
