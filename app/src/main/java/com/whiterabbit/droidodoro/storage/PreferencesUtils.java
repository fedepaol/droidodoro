package com.whiterabbit.droidodoro.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.whiterabbit.droidodoro.timer.TimerPresenterImpl;

/**
 * Created by fedepaol on 07/05/16.
 */
public class PreferencesUtils {
    private static final String TOKEN_ID = "com.whiterabbit.token";
    private static final String TODO_ID = "com.whiterabbit.todo";
    private static final String DOING_ID = "com.whiterabbit.doing";
    private static final String DONE_ID = "com.whiterabbit.done";
    private static final String STARTED_TIME_ID = "com.whiterabbit.time";
    private static final String TIME_TO_GO_ID = "com.whiterabbit.timetogo";
    private static final String TIMER_STATE_ID = "com.whiterabbit.timerstate";
    private static final String TASK_ID = "com.whiterabbit.timertask";


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

    public boolean needsToConfigure() {
        return getAuthToken().equals("") || getTodoList().equals("");
    }

    public boolean isTimerOngoing() {
        return !getTimerTaskId().equals("");
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

    public String getTodoList() {
        return mPreferences.getString(TODO_ID, "");
    }

    public String getDoingList() {
        return mPreferences.getString(DOING_ID, "");
    }

    public String getDoneList() {
        return mPreferences.getString(DONE_ID, "");
    }

    public void setStartedTime(long startTime) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(STARTED_TIME_ID, startTime);
        editor.apply();
    }

    public long getStartedTime() {
        return mPreferences.getLong(STARTED_TIME_ID, 0);
    }

    public void setTimeToGo(long timeToGo) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(TIME_TO_GO_ID, timeToGo);
        editor.apply();
    }

    public long getTimeToGo() {
        return mPreferences.getLong(TIME_TO_GO_ID, 0);
    }

    public void setTimerState(TimerPresenterImpl.TimerStateEnum state) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(TIMER_STATE_ID, state.ordinal());
        editor.apply();
    }

    public TimerPresenterImpl.TimerStateEnum getTimerState() {
        return TimerPresenterImpl.TimerStateEnum.values()[mPreferences.getInt(TIMER_STATE_ID, 0)];
    }

    public void saveTaskId(String taskId) {
        setString(TASK_ID, taskId);
    }

    public String getTimerTaskId() {
        return mPreferences.getString(TASK_ID, "");
    }
}
