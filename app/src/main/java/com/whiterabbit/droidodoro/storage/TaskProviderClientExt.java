package com.whiterabbit.droidodoro.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.whiterabbit.droidodoro.model.Card;

import java.util.List;


public class TaskProviderClientExt {
    private Context mContext;

    public TaskProviderClientExt(Context mContext) {
        this.mContext = mContext;
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
        return cr.update(TasksProvider.TASK_URI, contentValues, where, whereArgs);
    }
}
