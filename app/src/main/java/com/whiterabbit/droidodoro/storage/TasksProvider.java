/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.whiterabbit.droidodoro.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;

public class TasksProvider extends ContentProvider {
    private static final String TAG = "TasksProvider";

    protected static final String DATABASE_NAME = "Tasks.db";
    protected static final int DATABASE_VERSION = 1;

    // --------------- URIS --------------------
    public static final Uri TASK_URI = Uri.parse("content://com.whiterabbit.droidodoro/Task");
    
    // -------------- TASK DEFINITIONS ------------
    public static final String TASK_TABLE = "Task";

    public static final String TASK_IDENTIFIER_COLUMN = "Identifier";
    public static final int TASK_IDENTIFIER_COLUMN_POSITION = 1;
    public static final String TASK_DESCRIPTION_COLUMN = "Description";
    public static final int TASK_DESCRIPTION_COLUMN_POSITION = 2;
    public static final String TASK_LIST_COLUMN = "List";
    public static final int TASK_LIST_COLUMN_POSITION = 3;
    public static final String TASK_POMODOROS_COLUMN = "Pomodoros";
    public static final int TASK_POMODOROS_COLUMN_POSITION = 4;
    public static final String TASK_TIMESPENT_COLUMN = "TimeSpent";
    public static final int TASK_TIMESPENT_COLUMN_POSITION = 5;
    public static final String TASK_TOSYNCH_COLUMN = "ToSynch";
    public static final int TASK_TOSYNCH_COLUMN_POSITION = 6;
    public static final int ALL_TASK = 0;
    public static final int SINGLE_TASK = 1;

    

    public static final String ROW_ID = "_id";

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
        uriMatcher.addURI("com.whiterabbit.droidodoro", "Task", ALL_TASK);
        uriMatcher.addURI("com.whiterabbit.droidodoro", "Task/#", SINGLE_TASK);
    }
 

    // -------- TABLES CREATION ----------
    
    // Task CREATION 
    private static final String DATABASE_TASK_CREATE = "create table " + TASK_TABLE + " (" +
                                "_id integer primary key autoincrement, " +
                                TASK_IDENTIFIER_COLUMN + " text, " +
                                TASK_DESCRIPTION_COLUMN + " text, " +
                                TASK_LIST_COLUMN + " text, " +
                                TASK_POMODOROS_COLUMN + " integer, " +
                                TASK_TIMESPENT_COLUMN + " integer, " +
                                TASK_TOSYNCH_COLUMN + " integer" +
                                ")";
    

    protected DbHelper myOpenHelper;

    @Override
    public boolean onCreate() {
        myOpenHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    /**
    * Returns the right table name for the given uri
    * @param uri
    * @return
    */
    private String getTableNameFromUri(Uri uri){
        switch (uriMatcher.match(uri)) {
            case ALL_TASK:
            case SINGLE_TASK:
                return TASK_TABLE;
            default: break;
        }
        return null;
    }
    
    /**
    * Returns the parent uri for the given uri
    * @param uri
    * @return
    */
    private Uri getContentUriFromUri(Uri uri){
        switch (uriMatcher.match(uri)) {
            case ALL_TASK:
            case SINGLE_TASK:
                return TASK_URI;
            default: break;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder) {

        // Open thedatabase.
        SQLiteDatabase db;
        try {
            db = myOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = myOpenHelper.getReadableDatabase();
        }

        // Replace these with valid SQL statements if necessary.
        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_TASK:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(ROW_ID + "=" + rowID);
            default: break;
        }

        // Specify the table on which to perform the query. This can
        // be a specific table or a join as required.
        queryBuilder.setTables(getTableNameFromUri(uri));

        // Execute the query.
        Cursor cursor = queryBuilder.query(db, projection, selection,
                    selectionArgs, groupBy, having, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the result Cursor.
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // Return a string that identifies the MIME type
        // for a Content Provider URI
        switch (uriMatcher.match(uri)) {
            case ALL_TASK:
                return "vnd.android.cursor.dir/vnd.com.whiterabbit.droidodoro.storage.Task";
            case SINGLE_TASK:
                return "vnd.android.cursor.dir/vnd.com.whiterabbit.droidodoro.storage.Task";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_TASK:
                String rowID = uri.getPathSegments().get(1);
                selection = ROW_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ')' : "");
            default: break;
        }

        if (selection == null)
            selection = "1";

        int deleteCount = db.delete(getTableNameFromUri(uri),
                selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String nullColumnHack = null;

        long id = db.insert(getTableNameFromUri(uri), nullColumnHack, values);
        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(getContentUriFromUri(uri), id);
                                getContext().getContentResolver().notifyChange(insertedId, null);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        } else {
            return null;
        }
    }

    public int bulkInsert(Uri uri, ContentValues[] values){
        int numInserted = 0;

        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        db.beginTransaction();
        String table = getTableNameFromUri(uri);
        try {
            for (ContentValues cv : values) {
                long newID = db.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_TASK:
                String rowID = uri.getPathSegments().get(1);
                selection = ROW_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
            default: break;
        }

        // Perform the update.
        int updateCount = db.update(getTableNameFromUri(uri), values, selection, selectionArgs);
        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    protected static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one. 
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_TASK_CREATE);
        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log the version upgrade.
            Log.w(TAG, "Upgrading from version " + 
                        oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
            
            // Upgrade the existing database to conform to the new version. Multiple 
            // previous versions can be handled by comparing _oldVersion and _newVersion
            // values.

            // The simplest case is to drop the old table and create a new one.
            db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE + ";");
            // Create a new one.
            onCreate(db);
        }
    }
}

