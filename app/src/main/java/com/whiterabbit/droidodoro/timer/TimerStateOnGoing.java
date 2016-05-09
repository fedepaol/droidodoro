package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import java.util.Date;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TimerStateOnGoing extends TimerState {

    public TimerStateOnGoing(TimerView view, TimerPresenterImpl presenter) {
        super(view, presenter);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(true);
        mView.setPauseButtonText(R.string.timer_pause);
        mPresenter.saveTaskId(mView.getTaskId()); // as soon as I enter in this state, taskid is saved

        // there can be three scenarios here:
        // 1 - the user just pressed start
        // 2 - the user is coming back from pause
        // 3 - the user got back to the app and the timer was supposed to run

        long timeToGo = mPresenter.getTimeToGo();
        long started = mPresenter.getStartedTime();

        if (timeToGo == 0 && started == 0) { // case 1
            mPresenter.startCountdown(FIVE_MINUTES);
        } else if (started == 0){ // case 2
            mPresenter.startCountdown(timeToGo);
        } else {    // both are filled
            long now = new Date().getTime() * 1000;
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
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.FINISHED);
    }

    @Override
    public void onPauseRestartPressed() {
        mPresenter.stopCountDown();
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.PAUSED);
    }

    @Override
    public void onStopPressed() {
        TaskProviderClientExt providerClient = mPresenter.getProviderClient();
        Observable.fromCallable(() -> providerClient.updateTime(mView.getTaskId(),
                                        FIVE_MINUTES - mPresenter.getTimeToGo() + mPresenter.getTimeSpent()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> {},
                                               e -> {},
                                            () -> {
                                                mPresenter.stopCountDown();
                                                mPresenter.reloadValues();
                                                mPresenter.resetTimer();
                                                mPresenter.setTimeToGo(FIVE_MINUTES);
                                                mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
                                            });
    }

    @Override
    public void onBackPressed() {
        onStopPressed();
    }

}
