package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import com.whiterabbit.droidodoro.R;

import java.util.Date;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStatePaused extends TimerState {
    public TimerStatePaused(TimerView view, TimerPresenterImpl presenter) {
        super(view, presenter);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(true);
        mView.setPauseButtonText(R.string.timer_restart);
        mPresenter.resetDateStart();
    }

    @Override
    public void onPauseRestartPressed() {
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.RUNNING);
    }


}
