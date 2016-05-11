package com.whiterabbit.droidodoro.screens.configuration;


import com.whiterabbit.droidodoro.storage.KeyValueStorage;
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
                                                         KeyValueStorage u,
                                                         TaskProviderClientExt c){
        return new ConfigurationPresenterImpl(v, t, u, c);
    }
}
