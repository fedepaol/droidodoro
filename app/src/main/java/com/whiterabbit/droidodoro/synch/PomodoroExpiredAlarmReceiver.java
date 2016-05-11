package com.whiterabbit.droidodoro.synch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fedepaol on 11/05/16.
 */
public class PomodoroExpiredAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // can't update the db from here since we are in main thread. Need to be fast,
        // delegate the hard work to an intent service
        Intent i = new Intent(context, UpdatePomodorosService.class);
        context.startService(i);
    }
}
