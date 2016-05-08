package com.whiterabbit.droidodoro.configuration;


import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.trelloclient.TrelloClient;

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
                                                         TrelloClient t,
                                                         PreferencesUtils u,
                                                         TaskProviderClientExt c){
        return new ConfigurationPresenterImpl(v, t, u, c);
    }
}
