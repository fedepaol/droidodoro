package com.whiterabbit.droidodoro.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fedepaol on 07/05/16.
 */
public class PreferencesUtils {
    private static final String TOKEN_ID = "com.whiterabbit.token";
    private static final String TODO_ID = "com.whiterabbit.todo";
    private static final String DOING_ID = "com.whiterabbit.doing";
    private static final String DONE_ID = "com.whiterabbit.done";

    private SharedPreferences mPreferences;

    public PreferencesUtils(Context c) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    private void setString(String name, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public void setAuthToken(String token) {
        setString(TOKEN_ID, token);
    }

    public String getAuthToken() {
        return mPreferences.getString(TOKEN_ID, "");
    }

    public void setTodoList(String todo) {
        setString(TODO_ID, todo);
    }

    public void setDoingList(String doing) {
        setString(DOING_ID, doing);
    }

    public void setDoneList(String done) {
        setString(DONE_ID, done);
    }
}
