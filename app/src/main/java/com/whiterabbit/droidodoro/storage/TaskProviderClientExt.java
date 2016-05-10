package com.whiterabbit.droidodoro.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.whiterabbit.droidodoro.model.Card;
import com.whiterabbit.droidodoro.synch.TrelloSynchService;

import java.util.List;


public class TaskProviderClientExt {
    private Context mContext;
    private final static String SYNCH_TAG = "synch";

    public TaskProviderClientExt(Context mContext) {
        this.mContext = mContext;
    }

    private void scheduleSynch() {
        OneoffTask task = new OneoffTask.Builder()
                .setService(TrelloSynchService.class)
                .setTag(SYNCH_TAG)
                .setExecutionWindow(0L, 3600L)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .build();
        GcmNetworkManager.getInstance(mContext).schedule(task);
    }

    public int addCards(List<Card> cards, String listId) {
        ContentValues[] values = new ContentValues[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            Card toPut = cards.get(i);
            values[i] = new ContentValues();
            values[i].put(TasksProvider.TASK_IDENTIFIER_COLUMN, toPut.getId());
            values[i].put(TasksProvider.TASK_DESCRIPTION_COLUMN, toPut.getName());
            values[i].put(TasksProvider.TASK_LIST_COLUMN, listId);
            values[i].put(TasksProvider.TASK_POMODOROS_COLUMN, 0);
            values[i].put(TasksProvider.TASK_TIMESPENT_COLUMN, 0);
            values[i].put(TasksProvider.TASK_TOSYNCH_COLUMN, 0);
        }
        ContentResolver cr = mContext.getContentResolver();
        return cr.bulkInsert(TasksProvider.TASK_URI, values);
    }

    public int removeAllTask(){
        ContentResolver cr = mContext.getContentResolver();
        return cr.delete(TasksProvider.TASK_URI, null, null);
    }

    public int moveTaskToOtherList(String taskId, String doingListId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_LIST_COLUMN, doingListId);
        contentValues.put(TasksProvider.TASK_TOSYNCH_COLUMN, 1);

        ContentResolver cr = mContext.getContentResolver();
        String where = TasksProvider.TASK_IDENTIFIER_COLUMN + " = ?";
        String[] whereArgs = {taskId};
        int res = cr.update(TasksProvider.TASK_URI, contentValues, where, whereArgs);
        scheduleSynch();
        return res;
    }

    public int updateTime(String taskId, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_TIMESPENT_COLUMN, time);
        contentValues.put(TasksProvider.TASK_TOSYNCH_COLUMN, 1);

        ContentResolver cr = mContext.getContentResolver();
        String where = TasksProvider.TASK_IDENTIFIER_COLUMN + " = ?";
        String[] whereArgs = {taskId};
        return cr.update(TasksProvider.TASK_URI, contentValues, where, whereArgs);
    }

    public int updateTimeAndPomodoros(String taskId, long time, long pomodoros) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_TIMESPENT_COLUMN, time);
        contentValues.put(TasksProvider.TASK_POMODOROS_COLUMN, pomodoros);

        ContentResolver cr = mContext.getContentResolver();
        String where = TasksProvider.TASK_IDENTIFIER_COLUMN + " = ?";
        String[] whereArgs = {taskId};
        return cr.update(TasksProvider.TASK_URI, contentValues, where, whereArgs);
    }

    public Cursor getTask(String taskId) {
        ContentResolver cr = mContext.getContentResolver();
        String[] resultColumns = new String[] {
                TasksProvider.ROW_ID,
                TasksProvider.TASK_IDENTIFIER_COLUMN,
                TasksProvider.TASK_DESCRIPTION_COLUMN,
                TasksProvider.TASK_POMODOROS_COLUMN,
                TasksProvider.TASK_TIMESPENT_COLUMN
        };

        String where = TasksProvider.TASK_IDENTIFIER_COLUMN + " = ?";
        String[] whereArgs = {taskId};

        Cursor resultCursor = cr.query(TasksProvider.TASK_URI, resultColumns, where, whereArgs, null);
        return resultCursor;
    }

    public static Cursor getTasksToSynch(Context c) {
        ContentResolver cr = c.getContentResolver();
        String[] resultColumns = new String[] {
                TasksProvider.ROW_ID,
                TasksProvider.TASK_IDENTIFIER_COLUMN,
                TasksProvider.TASK_DESCRIPTION_COLUMN,
                TasksProvider.TASK_POMODOROS_COLUMN,
                TasksProvider.TASK_TIMESPENT_COLUMN,
                TasksProvider.TASK_LIST_COLUMN
        };

        String where = TasksProvider.TASK_TOSYNCH_COLUMN + " = ?";
        String[] whereArgs = {"1"};
        Cursor resultCursor = cr.query(TasksProvider.TASK_URI, resultColumns, where, whereArgs, null);
        return resultCursor;
    }

    public static int setSynchDone(Context c) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_TOSYNCH_COLUMN, 0);

        ContentResolver cr = c.getContentResolver();
        String where = null;
        String[] whereArgs = {};
        return cr.update(TasksProvider.TASK_URI, contentValues, where, whereArgs);
    }

}
