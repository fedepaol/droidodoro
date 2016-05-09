package com.whiterabbit.droidodoro.timer;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateBreak extends TimerState {
    public TimerStateBreak(TimerView view, TimerPresenterImpl presenter) {
        super(view, presenter);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(true);
        mView.toggleTimerGoingControls(false);

        mPresenter.startCountdown(mPresenter.getTimeToGo());
    }

    @Override
    public void onTimerFinished() {
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
    }

    @Override
    public void onStopBreakPressed() {
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
    }
}
