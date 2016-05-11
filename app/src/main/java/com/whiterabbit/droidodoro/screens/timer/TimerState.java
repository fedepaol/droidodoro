package com.whiterabbit.droidodoro.screens.timer;

import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.ListType;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/* Since the timer handling state is pretty complex this represent the class that handles
   the various events depending on the state the timer is currently on
 */
public abstract class TimerState implements TimerPresenter {
    static public final long FIVE_MINUTES = 15; // TODO Change to 5 minutes

    TimerView mView;
    TimerPresenterImpl mPresenter;
    KeyValueStorage mPreferences;
    TaskProviderClientExt mProviderClient;

    public TimerState(TimerView view,
                      TimerPresenterImpl presenter,
                      KeyValueStorage preferences,
                      TaskProviderClientExt client) {
        mView = view;
        mPresenter = presenter;
        mPreferences = preferences;
        mProviderClient = client;
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
        mPreferences.saveTaskId("");
        Observable.fromCallable(() -> mProviderClient.moveTaskToOtherList(mView.getTaskId(),
                mPreferences.getTodoList(),
                ListType.TODO))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mPresenter.resetTimer();
        mPresenter.stopCountDown();
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
