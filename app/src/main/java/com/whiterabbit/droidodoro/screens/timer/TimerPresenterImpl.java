package com.whiterabbit.droidodoro.screens.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.CountDownTimer;

import com.whiterabbit.droidodoro.Utils;
import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.storage.TasksProvider;
import com.whiterabbit.droidodoro.synch.PomodoroExpiredAlarmReceiver;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TimerPresenterImpl implements TimerPresenter {
    private static final String ALARM_ID = "TrelloAlarm";
    public enum TimerStateEnum {
        STOPPED,
        RUNNING,
        PAUSED,
        FINISHED,
        BREAK
    }

    private TimerView mView;
    private KeyValueStorage mKeyValueStorage;
    private TaskProviderClientExt mProviderClient;
    private Context mContext;

    private String taskName;


    private long timeSpent;
    private int pomodoros;
    private TimerState mState;
    private CountDownTimer mCountDownTimer;
    private TimerStateToStart mStateToStart;
    private TimerStateOnGoing mStateOngoing;
    private TimerStatePaused mStatePaused;
    private TimerStateFinished mStateFinished;
    private TimerStateBreak mStateBreak;


    public TimerPresenterImpl(TimerView v,
                              KeyValueStorage u,
                              TaskProviderClientExt c,
                              Context context) {
        mView = v;
        mKeyValueStorage = u;
        mProviderClient = c;
        mContext = context;
        mStateToStart = new TimerStateToStart(mView, this, u, c);
        mStateOngoing = new TimerStateOnGoing(mView, this, u, c);
        mStatePaused = new TimerStatePaused(mView, this, u, c);
        mStateFinished = new TimerStateFinished(mView, this, u, c);
        mStateBreak = new TimerStateBreak(mView, this, u, c);

        reloadValues();
    }

    public void reloadValues() {
        Observable.fromCallable(() -> mProviderClient.getTask(mView.getTaskId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::manageCursorResult);
    }

    private void manageCursorResult(Cursor c) {
        c.moveToFirst();
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
        stopCountDown();
        mState.onPause();
    }

    @Override
    public void onResume() {
        reloadValues();
        if (mKeyValueStorage.getTimerTaskId().equals("")) {
            setState(TimerStateEnum.STOPPED);
        } else {
            setState(mKeyValueStorage.getTimerState());
        }
    }

    private TimerState getTimerStateFromEnum(TimerStateEnum e) {
        switch (e) {
            case STOPPED:
                return mStateToStart;
            case RUNNING:
                return mStateOngoing;
            case PAUSED:
                return mStatePaused;
            case FINISHED:
                return mStateFinished;
            case BREAK:
                return mStateBreak;
        }
        return null;
    }

    @Override
    public void onStartPressed() {
        mState.onStartPressed();
    }

    @Override
    public void onPauseRestartPressed() {
        mState.onPauseRestartPressed();
    }

    @Override
    public void onStopPressed() {
        mState.onStopPressed();
    }

    @Override
    public void onBackPressed() {
        mState.onBackPressed();
    }

    @Override
    public void onDonePressed() {
        mState.onDonePressed();
    }

    @Override
    public void onShortBreakPressed() {
        mState.onShortBreakPressed();
    }

    @Override
    public void onLongBreakPressed() {
        mState.onLongBreakPressed();
    }

    @Override
    public void onStopBreakPressed() {
        mState.onStopBreakPressed();
    }

    public void setState(TimerStateEnum e) {
        if (mState != null) {
            mState.onExitState();
        }
        mState = getTimerStateFromEnum(e);
        mKeyValueStorage.setTimerState(e);
        mState.onEnterState();
    }

    public void resetTimer() {
        mKeyValueStorage.setStartedTime(0);
        mKeyValueStorage.setTimeToGo(0);
    }

    public void startCountdown(long howLong) {
        mCountDownTimer = new CountDownTimer(howLong * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                mView.setCurrentTime(millisUntilFinished / 1000);
                mKeyValueStorage.setTimeToGo(millisUntilFinished / 1000); // TODO This can be optimized
            }

            public void onFinish() {
                mState.onTimerFinished();
            }
        }.start();
    }

    public void stopCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public long getPomodoros() {
        return pomodoros;
    }

    public void setAlarm(long toFinish) {
        long wakeUpTime = Utils.getNowMillis() + toFinish * 1000;
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, PomodoroExpiredAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
        }
    }

    public void removeAlarm() {
        Intent intent = new Intent(mContext, PomodoroExpiredAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }
}
