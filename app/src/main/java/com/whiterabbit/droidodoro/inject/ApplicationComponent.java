package com.whiterabbit.droidodoro.inject;

/**
 * Created by fedepaol on 28/06/15.
 */

import android.content.Context;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.trelloclient.TrelloClient;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(DroidodoroApplication app);
    PreferencesUtils getPrefUtils();
    TrelloClient getTrelloClient();
    TaskProviderClientExt getProviderClient();
    Context getContext();
}

