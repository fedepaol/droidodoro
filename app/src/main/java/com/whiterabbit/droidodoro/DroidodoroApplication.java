package com.whiterabbit.droidodoro;

import android.app.Application;

import com.whiterabbit.droidodoro.inject.ApplicationComponent;
import com.whiterabbit.droidodoro.inject.ApplicationModule;
import com.whiterabbit.droidodoro.inject.DaggerApplicationComponent;

public class DroidodoroApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    void initComponent() {
        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(getApplicationModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
