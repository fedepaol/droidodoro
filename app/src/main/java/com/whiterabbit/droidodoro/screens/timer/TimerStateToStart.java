package com.whiterabbit.droidodoro.screens.timer;

import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateToStart extends TimerState {
    public TimerStateToStart(TimerView view,
                             TimerPresenterImpl presenter,
                             KeyValueStorage prefs,
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
        mKeyValueStorage.saveTaskId("");
        mView.setCurrentTime(FIFTEEN_MINUTES);
    }
}
