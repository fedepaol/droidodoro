package com.whiterabbit.droidodoro.screens.timer;

import android.media.MediaPlayer;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.Utils;
import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TimerStateOnGoing extends TimerState {
    boolean mBackWasPressed;

    public TimerStateOnGoing(TimerView view,
                             TimerPresenterImpl presenter,
                             KeyValueStorage prefs,
                             TaskProviderClientExt client) {
        super(view, presenter, prefs, client);
    }

    private boolean isFirstStart(long timeToGo, long started) {
        return (timeToGo == 0 && started == 0);
    }

    private boolean isResume(long timeToGo, long started) {
        return (timeToGo != 0 && started == 0);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(true);
        mView.setPauseButtonText(R.string.timer_pause);
        mKeyValueStorage.saveTaskId(mView.getTaskId()); // as soon as I enter in this state, taskid is saved
        mPresenter.removeAlarm();
        mBackWasPressed = false;

        // there can be three scenarios here:
        // 1 - the user just pressed start
        // 2 - the user is coming back from pause
        // 3 - the user got back to the app and the timer was supposed to run

        long timeToGo = mKeyValueStorage.getTimeToGo();
        long started = mKeyValueStorage.getStartedTime();

        if (isFirstStart(timeToGo, started)) { // case 1
            mKeyValueStorage.setStartedTime(Utils.getNowMillis() / 1000); // First start
            mPresenter.startCountdown(FIFTEEN_MINUTES);
        } else if (isResume(timeToGo, started)){ // case 2
            mPresenter.startCountdown(timeToGo);
        } else {    // both are filled
            long now = Utils.getNowMillis() / 1000;
            long spent = now - started;
            if (spent >= FIFTEEN_MINUTES) {
                mPresenter.setState(TimerPresenterImpl.TimerStateEnum.FINISHED);
            } else {
                mPresenter.startCountdown(FIFTEEN_MINUTES - spent);
            }
        }
    }

    @Override
    public void onTimerFinished() {
        final MediaPlayer mp = MediaPlayer.create(mView.getContext(), R.raw.applause);
        mp.start();
        Observable.fromCallable(() -> mProviderClient.updateTimeAndPomodoros(mView.getTaskId(),
                FIFTEEN_MINUTES + mPresenter.getTimeSpent(), mPresenter.getPomodoros() + 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                        e -> {},
                        () ->{
                            mPresenter.setState(TimerPresenterImpl.TimerStateEnum.FINISHED);
                            mPresenter.reloadValues();
                        });
    }

    @Override
    public void onPauseRestartPressed() {
        mPresenter.stopCountDown();
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.PAUSED);
    }

    @Override
    public void onStopPressed() {
        Observable.fromCallable(() -> mProviderClient.updateTime(mView.getTaskId(),
                                        FIFTEEN_MINUTES - mKeyValueStorage.getTimeToGo() + mPresenter.getTimeSpent()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> {},
                                               e -> {},
                                            () -> {
                                                mPresenter.stopCountDown();
                                                mPresenter.reloadValues();
                                                mPresenter.resetTimer();
                                                mKeyValueStorage.setTimeToGo(FIFTEEN_MINUTES);
                                                mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
                                            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBackWasPressed = true;
    }

    @Override
    public void onPause() {
        if (!mBackWasPressed) // awful hack to check if the app is being closed
            mPresenter.setAlarm(mKeyValueStorage.getTimeToGo());
        super.onPause();
    }
}
