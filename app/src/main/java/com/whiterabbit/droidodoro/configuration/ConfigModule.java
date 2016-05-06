package com.whiterabbit.droidodoro.configuration;


import com.whiterabbit.droidodoro.storage.PreferencesUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {
    private ConfigurationView mView;
    public ConfigModule(ConfigurationView view) {
        mView = view;
    }

    @Provides
    public ConfigurationView provideDetailView() {
        return mView;
    }


    @Provides
    public ConfigurationPresenter provideConfigPresenter(ConfigurationView v,
                                                         PreferencesUtils u){
        return new ConfigurationPresenterImpl(v, u);
    }
}
