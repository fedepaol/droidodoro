package com.whiterabbit.droidodoro.timer;

import android.content.Context;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;

import dagger.Module;
import dagger.Provides;

@Module
public class TimerModule {
    private TimerView mView;
    public TimerModule(TimerView view) {
        mView = view;
    }

    @Provides
    public TimerView provideTimerView() {
        return mView;
    }


    @Provides
    public TimerPresenter provideTimerPresenter(TimerView v,
                                                         PreferencesUtils u,
                                                         TaskProviderClientExt c,
                                                         Context context){
        return new TimerPresenterImpl(v, u, c, context);
    }
}
