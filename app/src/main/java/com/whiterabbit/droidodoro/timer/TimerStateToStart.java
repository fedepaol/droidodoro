package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import java.util.Date;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateToStart extends TimerState {
    public TimerStateToStart(TimerView view,
                             TimerPresenterImpl presenter,
                             PreferencesUtils prefs,
                             TaskProviderClientExt client) {
        super(view, presenter, prefs, client);
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
        mPreferences.saveTaskId("");
        mView.setCurrentTime(FIVE_MINUTES);
    }
}
