package com.whiterabbit.droidodoro.timer;

import android.content.Context;

/**
 * Created by fedepaol on 08/05/16.
 */
public interface TimerView {
    String getTaskId();
    void setTaskValues(String title, int pomodoros, long totalTime);

    void toggleToStartControls(boolean show);
    void toggleTimerGoingControls(boolean show);
    void toggleTimerFinishedControls(boolean show);
    void toggleBreakControls(boolean show);
    void setPauseButtonText(int resid);
    void setCurrentTime(long secondsToGo);
    void closeView();
    Context getContext();
}
