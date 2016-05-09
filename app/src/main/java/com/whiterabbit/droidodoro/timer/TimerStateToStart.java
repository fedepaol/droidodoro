package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import java.util.Date;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateToStart extends TimerState {
    public TimerStateToStart(TimerView view, TimerPresenterImpl presenter) {
        super(view, presenter);
    }

    @Override
    public void onStartPressed() {
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.RUNNING);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(true);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(false);
        mPresenter.saveTaskId("");
    }
}
