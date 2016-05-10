package com.whiterabbit.droidodoro.synch;

import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.Utils;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.storage.TasksProvider;
import com.whiterabbit.droidodoro.trelloclient.TrelloClient;

import java.io.IOException;

/* Service to be scheduled. It updates on trello all the tasks marked as "to synch", then
   sets their flag to 0
 */
public class TrelloSynchService extends GcmTaskService {
    private String buildComment(long seconds, long pomodoros) {
        return String.format(getString(R.string.trello_comment), pomodoros,
                                                                 Utils.getTimeFromSeconds(seconds));
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d("SYNCH", "Started synch");
        PreferencesUtils preferences = new PreferencesUtils(this);
        TrelloClient trello = new TrelloClient(preferences);

        Cursor tasksToSynch = TaskProviderClientExt.getTasksToSynch(this);
        if (tasksToSynch.getCount() == 0) {
            tasksToSynch.close();
            return GcmNetworkManager.RESULT_SUCCESS;
        }

        tasksToSynch.moveToFirst();
        do {
            int idIndex = tasksToSynch.getColumnIndex(TasksProvider.TASK_IDENTIFIER_COLUMN);
            String taskId = tasksToSynch.getString(idIndex);
            int timeColumnIndx = tasksToSynch.getColumnIndex(TasksProvider.TASK_TIMESPENT_COLUMN);
            long seconds = tasksToSynch.getLong(timeColumnIndx);
            int pomodorosIndx = tasksToSynch.getColumnIndex(TasksProvider.TASK_POMODOROS_COLUMN);
            long pomodoros = tasksToSynch.getLong(pomodorosIndx);
            int listIndx = tasksToSynch.getColumnIndex(TasksProvider.TASK_LIST_COLUMN);
            String list = tasksToSynch.getString(listIndx);

            try {
                trello.updateCard(taskId, list);
                if (list.equals(preferences.getDoneList())) {
                    String comment = buildComment(seconds, pomodoros);
                    trello.setCommentToCard(taskId, comment);
                }
            } catch (IOException e) {
                tasksToSynch.close();
                return GcmNetworkManager.RESULT_RESCHEDULE;
            }
        } while (tasksToSynch.moveToNext());
        tasksToSynch.close();
        TaskProviderClientExt.setSynchDone(this);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}