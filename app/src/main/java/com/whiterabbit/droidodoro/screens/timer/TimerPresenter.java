package com.whiterabbit.droidodoro.screens.timer;

/**
 * Created by fedepaol on 08/05/16.
 */
public interface TimerPresenter {
    void onPause();
    void onResume();
    void onStartPressed();
    void onPauseRestartPressed();
    void onStopPressed();
    void onBackPressed();
    void onDonePressed();
    void onShortBreakPressed();
    void onLongBreakPressed();
    void onStopBreakPressed();
}
