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
        Intent i = new Intent(context, TasksActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setContentTitle(context.getString(R.string.pomodoro_finished))
                .setAutoCancel(true)
                .setContentText(context.getString(R.string.pomodoro_task_finished))
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentIntent(pIntent);

        Notification n = b.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, n);
    }
}
