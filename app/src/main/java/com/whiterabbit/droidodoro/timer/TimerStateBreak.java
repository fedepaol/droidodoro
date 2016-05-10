package com.whiterabbit.droidodoro.timer;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateBreak extends TimerState {
    public TimerStateBreak(TimerView view,
                           TimerPresenterImpl presenter,
                           PreferencesUtils prefs,
                           TaskProviderClientExt client) {
        super(view, presenter, prefs, client);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(true);
        mView.toggleTimerGoingControls(false);

        mPresenter.startCountdown(mPreferences.getTimeToGo());
    }

    @Override
    public void onTimerFinished() {
        mPresenter.resetTimer();
        mPresenter.stopCountDown();
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
    }

    @Override
    public void onStopBreakPressed() {
        mPresenter.resetTimer();
        mPresenter.stopCountDown();
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.STOPPED);
    }
}
