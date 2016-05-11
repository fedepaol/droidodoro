package com.whiterabbit.droidodoro.screens.configuration;

/**
 * Created by fedepaol on 28/06/15.
 */


import com.whiterabbit.droidodoro.inject.ActivityScope;
import com.whiterabbit.droidodoro.inject.ApplicationComponent;

import dagger.Component;

@ActivityScope
@Component(modules = {ConfigModule.class},
           dependencies = {ApplicationComponent.class})
public interface ConfigComponent {
    void inject(ConfigurationFragment f);
}

