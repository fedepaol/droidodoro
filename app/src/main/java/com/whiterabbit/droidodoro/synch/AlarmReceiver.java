package com.whiterabbit.droidodoro.synch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.tasks.TasksActivity;

/**
 * Created by fedepaol on 11/05/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // can't update the db from here since we are in main thread. Need to be fast,
        // delegate the hard work to an intent service
        Intent i = new Intent(context, UpdatePomodorosService.class);
        context.startService(i);
    }
}
