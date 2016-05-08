package com.whiterabbit.droidodoro.tasks;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.storage.TasksProvider;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public long _id;
        public boolean mIsFavourite;
        @BindView(R.id.task_name) TextView mName;
        @BindView(R.id.task_time) TextView mTimeSpent;
        @BindView(R.id.task_pomodoros) TextView mPomodoros;
        String mTaskId;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    Cursor mTasks;
    Context mContext;

    public TasksAdapter(Context c) {
        mContext = c;
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_elem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mTasks.moveToPosition(position);
        int nameColumnIndx = mTasks.getColumnIndex(TasksProvider.TASK_DESCRIPTION_COLUMN);
        String name = mTasks.getString(nameColumnIndx);
        int timeColumnIndx = mTasks.getColumnIndex(TasksProvider.TASK_TIMESPENT_COLUMN);
        long seconds = mTasks.getLong(timeColumnIndx);
        int pomodorosIndx = mTasks.getColumnIndex(TasksProvider.TASK_POMODOROS_COLUMN);
        long pomodoros = mTasks.getLong(pomodorosIndx);
        holder.mName.setText(name);
        holder.mTimeSpent.setText(String.valueOf(seconds));
        holder.mPomodoros.setText(String.valueOf(pomodoros));
        int idIndext = mTasks.getColumnIndex(TasksProvider.TASK_IDENTIFIER_COLUMN);
        holder.mTaskId = mTasks.getString(idIndext);
    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        }
        return mTasks.getCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void setTasks(Cursor c) {
        mTasks = c;
        notifyDataSetChanged();
    }
 }
