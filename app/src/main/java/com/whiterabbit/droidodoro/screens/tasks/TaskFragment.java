package com.whiterabbit.droidodoro.screens.tasks;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.storage.ListType;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
import com.whiterabbit.droidodoro.storage.TasksProvider;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class TaskFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,TasksAdapter.TaskSelectedCallback {
    @Inject
    KeyValueStorage mPreferences;
    @Inject
    Context mContext;
    @Inject
    TaskProviderClientExt mProviderClient;

    @BindView(R.id.tasks_list) RecyclerView mTasksList;
    TasksAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DroidodoroApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.tasks_fragment, container, false);
        ButterKnife.bind(this, res);
        return res;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTasksList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mTasksList.setLayoutManager(layoutManager);
        mTasksList.setNestedScrollingEnabled(false);
        mAdapter = new TasksAdapter(mContext, this);
        mTasksList.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(getLoaderId(), null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = null;
        String where = "ListType = ?";
        String[] whereArgs = {String.valueOf(getListType().ordinal())};
        String sortOrder = "TimeSpent desc";

        // Query URI
        Uri queryUri = TasksProvider.TASK_URI;
        return new CursorLoader(mContext, queryUri, projection, where, whereArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setTasks(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setTasks(null);
    }

    abstract ListType getListType();

    abstract int getLoaderId();

    @Override
    public void onTaskSelected(String taskId) {
        // do nothing by default, will be implemented only in
        // todo tasks.
    }
}
