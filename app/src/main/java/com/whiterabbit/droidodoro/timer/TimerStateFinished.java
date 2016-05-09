package com.whiterabbit.droidodoro.timer;


import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fedepaol on 09/05/16.
 */
public class TimerStateFinished extends TimerState {
    TaskProviderClientExt providerClient;
    public TimerStateFinished(TimerView view, TimerPresenterImpl presenter) {
        super(view, presenter);
        providerClient = mPresenter.getProviderClient();
    }

    @Override
    void onEnterState() {
        mView.toggleToStartControls(false);
        mView.toggleTimerFinishedControls(true);
        mView.toggleBreakControls(false);
        mView.toggleTimerGoingControls(false);
        mView.setCurrentTime(0);
        mPresenter.resetTimer();

        Observable.fromCallable(() -> providerClient.updateTimeAndPomodoros(mView.getTaskId(),
                FIVE_MINUTES + mPresenter.getTimeSpent(), mPresenter.getPomodoros() + 1
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                        e -> {},
                        () -> mPresenter.reloadValues());
    }

    @Override
    public void onDonePressed() {
        Observable.fromCallable(() -> providerClient.moveTaskToOtherList(mView.getTaskId(),
                                                                         mPresenter.getDoneList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                        e -> {},
                        () -> mView.closeView());
        mPresenter.saveTaskId("");
    }

    @Override
    public void onShortBreakPressed() {
        mPresenter.setTimeToGo(5 * 60);
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.BREAK);
    }

    @Override
    public void onLongBreakPressed() {
        mPresenter.setTimeToGo(15 * 60);
        mPresenter.setState(TimerPresenterImpl.TimerStateEnum.BREAK);
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveTaskId("");
        Observable.fromCallable(() -> providerClient.moveTaskToOtherList(mView.getTaskId(),
            mPresenter.getTodoList()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe();
    }
}
