package com.whiterabbit.droidodoro.timer;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import java.util.Date;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TimerStateOnGoing extends TimerState {

    public TimerStateOnGoing(TimerView view,
                             TimerPresenterImpl presenter,
                             PreferencesUtils prefs,
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
        mPreferences.saveTaskId(mView.getTaskId()); // as soon as I enter in this state, taskid is saved

        // there can be three scenarios here:
        // 1 - the user just pressed start
        // 2 - the user is coming back from pause
        // 3 - the user got back to the app and the timer was supposed to run

        long timeToGo = mPreferences.getTimeToGo();
        long started = mPreferences.getStartedTime();

        if (isFirstStart(timeToGo, started)) { // case 1
            mPresenter.startCountdown(FIVE_MINUTES);
        } else if (isResume(timeToGo, started)){ // case 2
            mPresenter.startCountdown(timeToGo);
        } else {    // both are filled
            long now = new Date().getTime() / 1000;
            long spent = now - started;
            if (spent >= timeToGo) {
                mPresenter.setState(TimerPresenterImpl.TimerStateEnum.FINISHED);
            } else {
                mPresenter.startCountdown(timeToGo - spent);
            }
        }
    }

    @Override
    public void onTimerFinished() {
        Observable.fromCallable(() -> mProviderClient.updateTimeAndPomodoros(mView.getTaskId(),
                FIVE_MINUTES + mPresenter.getTimeSpent(), mPresenter.getPomodoros() + 1
        ))
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
                                        FIVE_MINUTES - mPreferences.getTimeToGo() + mPresenter.getTimeSpent()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> {},
                                               e -> {},
                                            () -> {
                                                mPresenter.stopCountDown();
                                                mPresenter.reloadValues();
                                                mPresenter.resetTimer();
                                                mPreferences.setTimeToGo(FIVE_MINUTES);
                                                mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
                                            });
    }

    @Override
    public void onBackPressed() {
        onStopPressed();
    }

}
