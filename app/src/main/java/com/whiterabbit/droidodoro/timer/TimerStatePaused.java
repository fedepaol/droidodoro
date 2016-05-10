package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import java.util.Date;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStatePaused extends TimerState {
    public TimerStatePaused(TimerView view,
                            TimerPresenterImpl presenter,
                            PreferencesUtils prefs,
                            TaskProviderClientExt client) {
        super(view, presenter, prefs, client);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(false);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(true);
        mView.setPauseButtonText(R.string.timer_restart);
        mPreferences.setStartedTime(0);
    }

    @Override
    public void onPauseRestartPressed() {
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.RUNNING);
    }


}
