/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.whiterabbit.droidodoro.storage;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

public class TasksProviderClient {


    // ------------- TASK_HELPERS ------------
    public static Uri addTask (String Identifier, 
                                String Description, 
                                String List, 
                                Long Pomodoros, 
                                Long TimeSpent, 
                                Integer ToSynch, 
                                Context c) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_IDENTIFIER_COLUMN, Identifier);
        contentValues.put(TasksProvider.TASK_DESCRIPTION_COLUMN, Description);
        contentValues.put(TasksProvider.TASK_LIST_COLUMN, List);
        contentValues.put(TasksProvider.TASK_POMODOROS_COLUMN, Pomodoros);
        contentValues.put(TasksProvider.TASK_TIMESPENT_COLUMN, TimeSpent);
        contentValues.put(TasksProvider.TASK_TOSYNCH_COLUMN, ToSynch);
        ContentResolver cr = c.getContentResolver();
        return cr.insert(TasksProvider.TASK_URI, contentValues);
    }

    public static int removeTask(long rowIndex, Context c){
        ContentResolver cr = c.getContentResolver();
        Uri rowAddress = ContentUris.withAppendedId(TasksProvider.TASK_URI, rowIndex);
        return cr.delete(rowAddress, null, null);
    }

    public static int removeAllTask(Context c){
        ContentResolver cr = c.getContentResolver();
        return cr.delete(TasksProvider.TASK_URI, null, null);
    }

    public static Cursor getAllTask(Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] resultColumns = new String[] {
                         TasksProvider.ROW_ID,
                         TasksProvider.TASK_IDENTIFIER_COLUMN,
                         TasksProvider.TASK_DESCRIPTION_COLUMN,
                         TasksProvider.TASK_LIST_COLUMN,
                         TasksProvider.TASK_POMODOROS_COLUMN,
                         TasksProvider.TASK_TIMESPENT_COLUMN,
                         TasksProvider.TASK_TOSYNCH_COLUMN
                         };

        Cursor resultCursor = cr.query(TasksProvider.TASK_URI, resultColumns, null, null, null);
        return resultCursor;
    }

    public static Cursor getTask(long rowId, Context c){
    	ContentResolver cr = c.getContentResolver();
        String[] resultColumns = new String[] {
                         TasksProvider.ROW_ID,
                         TasksProvider.TASK_IDENTIFIER_COLUMN,
                         TasksProvider.TASK_DESCRIPTION_COLUMN,
                         TasksProvider.TASK_LIST_COLUMN,
                         TasksProvider.TASK_POMODOROS_COLUMN,
                         TasksProvider.TASK_TIMESPENT_COLUMN,
                         TasksProvider.TASK_TOSYNCH_COLUMN
                         };

        Uri rowAddress = ContentUris.withAppendedId(TasksProvider.TASK_URI, rowId);
        String where = null;    
        String whereArgs[] = null;
        String order = null;
    
        Cursor resultCursor = cr.query(rowAddress, resultColumns, where, whereArgs, order);
        return resultCursor;
    }

    public static int updateTask (int rowId, 
                                   String Identifier,
                                   String Description,
                                   String List,
                                   Long Pomodoros,
                                   Long TimeSpent,
                                   Integer ToSynch,
                                   Context c) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksProvider.TASK_IDENTIFIER_COLUMN, Identifier);
        contentValues.put(TasksProvider.TASK_DESCRIPTION_COLUMN, Description);
        contentValues.put(TasksProvider.TASK_LIST_COLUMN, List);
        contentValues.put(TasksProvider.TASK_POMODOROS_COLUMN, Pomodoros);
        contentValues.put(TasksProvider.TASK_TIMESPENT_COLUMN, TimeSpent);
        contentValues.put(TasksProvider.TASK_TOSYNCH_COLUMN, ToSynch);
        Uri rowAddress = ContentUris.withAppendedId(TasksProvider.TASK_URI, rowId);

        ContentResolver cr = c.getContentResolver();
        int updatedRowCount = cr.update(rowAddress, contentValues, null, null);
        return updatedRowCount;
    }
    
}
