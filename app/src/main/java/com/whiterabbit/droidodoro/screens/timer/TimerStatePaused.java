package com.whiterabbit.droidodoro.screens.timer;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStatePaused extends TimerState {
    public TimerStatePaused(TimerView view,
                            TimerPresenterImpl presenter,
                            KeyValueStorage prefs,
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
}
