package com.whiterabbit.droidodoro.timer;

import android.database.Cursor;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.storage.TasksProvider;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TimerPresenterImpl implements TimerPresenter {
    private TimerView mView;
    private PreferencesUtils mPreferences;
    private TaskProviderClientExt mProviderClient;

    private String taskName;
    private long timeSpent;
    private int pomodoros;

    public TimerPresenterImpl(TimerView v,
                              PreferencesUtils u,
                              TaskProviderClientExt c) {
        mView = v;
        mPreferences = u;
        mProviderClient = c;

        Observable.fromCallable(() -> mProviderClient.getTask(mView.getTaskId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::manageCursorResult);
    }

    private void manageCursorResult(Cursor c) {
        int nameIndx = c.getColumnIndex(TasksProvider.TASK_DESCRIPTION_COLUMN);
        int timeIndx = c.getColumnIndex(TasksProvider.TASK_TIMESPENT_COLUMN);
        int pomodorosIndx = c.getColumnIndex(TasksProvider.TASK_POMODOROS_COLUMN);

        taskName = c.getString(nameIndx);
        timeSpent = c.getLong(timeIndx);
        pomodoros = c.getInt(pomodorosIndx);
        c.close();

        mView.setTaskValues(taskName, pomodoros, timeSpent);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
