package com.whiterabbit.droidodoro.timer;


import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateFinished extends TimerState {
    public TimerStateFinished(TimerView view,
                              TimerPresenterImpl presenter,
                              PreferencesUtils prefs,
                              TaskProviderClientExt client) {
        super(view, presenter, prefs, client);
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(true);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(false);
        mView.setCurrentTime(0);
        mPresenter.resetTimer();


    }

    @Override
    public void onDonePressed() {
        Observable.fromCallable(() -> mProviderClient.moveTaskToOtherList(mView.getTaskId(),
                mPreferences.getDoneList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                        e -> {},
                        () -> mView.closeView());
        mPreferences.saveTaskId("");
    }

    @Override
    public void onShortBreakPressed() {
        mPreferences.setTimeToGo(5 * 60);
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.BREAK);
    }

    @Override
    public void onLongBreakPressed() {
        mPreferences.setTimeToGo(15 * 60);
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.BREAK);
    }

}
