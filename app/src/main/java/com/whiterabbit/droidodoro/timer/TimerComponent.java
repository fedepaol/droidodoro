package com.whiterabbit.droidodoro.timer;

/**
 * Created by fedepaol on 28/06/15.
 */


import com.whiterabbit.droidodoro.inject.ActivityScope;
import com.whiterabbit.droidodoro.inject.ApplicationComponent;

import dagger.Component;

@ActivityScope
@Component(modules = {TimerModule.class},
           dependencies = {ApplicationComponent.class})
public interface TimerComponent {
    void inject(TimerFragment f);
}

