package com.whiterabbit.droidodoro.inject;

/**
 * Created by fedepaol on 28/06/15.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.whiterabbit.droidodoro.DroidodoroApplication;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(DroidodoroApplication app);
    SharedPreferences getSharedPrefs();
    Context getContext();
}

