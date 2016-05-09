package com.whiterabbit.droidodoro.timer;

import android.content.Context;



/* Since the timer handling state is pretty complex this represent the class that handles
   the various events depending on the state the timer is currently on
 */
public class TimerState implements TimerPresenter {
    static final long FIVE_MINUTES = 15; // TODO Change to 5 minutes

    TimerView mView;
    TimerPresenterImpl mPresenter;

    public TimerState(TimerView view,
                      TimerPresenterImpl presenter) {
        mView = view;
        mPresenter = presenter;

    }

    void onEnterState() {

    }

    void onExitState() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStartPressed() {

    }

    @Override
    public void onPauseRestartPressed() {

    }

    @Override
    public void onStopPressed() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onShortBreakPressed() {

    }

    @Override
    public void onLongBreakPressed() {

    }

    @Override
    public void onStopBreakPressed() {

    }

    public void onTimerFinished() {

    }


}
