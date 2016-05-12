package com.whiterabbit.droidodoro.screens.tasks;


import android.content.Intent;

import com.whiterabbit.droidodoro.screens.timer.TimerActivity;
import com.whiterabbit.droidodoro.storage.ListType;

import rx.Observable;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TodoTasksFragment extends TaskFragment {
    private Subscription mSubscription;

    @Override
    ListType getListType() {
        return ListType.TODO;
    }

    @Override
    int getLoaderId() {
        return 0;
    }

    @Override
    public void onTaskSelected(String taskId) {
        mSubscription = Observable.fromCallable(() ->
                                        mProviderClient.moveTaskToOtherList(taskId,
                                                                            mKeyValueStorage.getDoingList(),
                                                                            ListType.DOING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                           e -> this.onUpdateError(e.getMessage()),
                            () -> this.startTimer(taskId));
    }

    private void startTimer(String taskId) {
        Intent i = new Intent(mContext, TimerActivity.class);
        i.putExtra(TimerActivity.TASK_PARAM, taskId);
        startActivity(i);
    }

    private void onUpdateError(String errMessage) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
