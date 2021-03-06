package com.whiterabbit.droidodoro.inject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.trelloclient.TrelloClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fedepaol on 28/06/15.
 */
@Module
public class ApplicationModule {
    private Application mApp;

    public ApplicationModule(Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    @Singleton
    KeyValueStorage provideKeyValueStorage() {
        return new KeyValueStorage(mApp);
    }

    @Provides
    @Singleton
    TrelloClient provideTrelloClient(KeyValueStorage u) {
        return new TrelloClient(u);
    }

    @Provides
    @Singleton
    TaskProviderClientExt provideStorageClient() {
        return new TaskProviderClientExt(mApp.getApplicationContext());
    }

}
