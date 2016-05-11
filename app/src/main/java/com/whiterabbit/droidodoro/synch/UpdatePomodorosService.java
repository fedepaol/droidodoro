package com.whiterabbit.droidodoro.synch;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.Utils;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.storage.TasksProvider;
import com.whiterabbit.droidodoro.tasks.TasksActivity;
import com.whiterabbit.droidodoro.timer.TimerState;

/**
 * Created by fedepaol on 11/05/16.
 */
public class UpdatePomodorosService extends IntentService {


    public UpdatePomodorosService() {
        super("pomodoros");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TaskProviderClientExt client = new TaskProviderClientExt(this);
        PreferencesUtils prefs = new PreferencesUtils(this);
        String taskId = prefs.getTimerTaskId();
        if (taskId.equals("")) {
            Log.e("Droidodoro", "End task broadcast on empty task");
            return;
        }
        Cursor c = client.getTask(taskId);
        if (c.getCount() <= 0) {
            Log.e("Droidodoro", "End task broadcast on not existent task " + taskId);
            c.close();
            return;
        }
        c.moveToFirst();
        int timeColumnIndx = c.getColumnIndex(TasksProvider.TASK_TIMESPENT_COLUMN);
        long seconds = c.getLong(timeColumnIndx);
        int pomodorosIndx = c.getColumnIndex(TasksProvider.TASK_POMODOROS_COLUMN);
        long pomodoros = c.getLong(pomodorosIndx);
        c.close();
        client.updateTimeAndPomodoros(prefs.getTimerTaskId(),  TimerState.FIVE_MINUTES + seconds, pomodoros + 1);

        Intent i = new Intent(this, TasksActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setContentTitle(this.getString(R.string.pomodoro_finished))
                .setAutoCancel(true)
                .setContentText(this.getString(R.string.pomodoro_task_finished))
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentIntent(pIntent);

        Notification n = b.build();
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, n);
    }
}
