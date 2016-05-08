package com.whiterabbit.droidodoro.timer;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

/**
 * Created by fedepaol on 08/05/16.
 */
public class TimerPresenterImpl implements TimerPresenter {
    private TimerView mView;
    private PreferencesUtils mPreferences;
    private TaskProviderClientExt mProviderClient;

    public TimerPresenterImpl(TimerView v,
                              PreferencesUtils u,
                              TaskProviderClientExt c) {
        mView = v;
        mPreferences = u;
        mProviderClient = c;
    }
}
