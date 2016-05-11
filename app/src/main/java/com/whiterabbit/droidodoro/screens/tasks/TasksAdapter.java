package com.whiterabbit.droidodoro.screens.tasks;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.Utils;
import com.whiterabbit.droidodoro.storage.TasksProvider;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    public interface TaskSelectedCallback {
        void onTaskSelected(String taskId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.task_name) TextView mName;
        @BindView(R.id.task_time) TextView mTimeSpent;
        @BindView(R.id.task_pomodoros) TextView mPomodoros;
        String mTaskId;
        TaskSelectedCallback mCallBack;

        public ViewHolder(View v, TaskSelectedCallback c) {
            super(v);
            ButterKnife.bind(this, v);
            mCallBack = c;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallBack.onTaskSelected(mTaskId);
        }
    }

    Cursor mTasks;
    Context mContext;
    TaskSelectedCallback mCallback;

    public TasksAdapter(Context c, TaskSelectedCallback callback) {
        mContext = c;
        mCallback = callback;
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_elem, parent, false);
        ViewHolder vh = new ViewHolder(v, mCallback);
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
        holder.mTimeSpent.setText(Utils.getTimeFromSeconds(seconds));
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
