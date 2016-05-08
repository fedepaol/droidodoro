package com.whiterabbit.droidodoro.tasks;


import rx.Observable;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TodoTasksFragment extends TaskFragment {
    private Subscription mSubscription;

    @Override
    String getListId() {
        return mPreferences.getTodoList();
    }

    @Override
    int getLoaderId() {
        return 0;
    }

    @Override
    public void onTaskSelected(String taskId) {
        mSubscription = Observable.fromCallable(() -> mProviderClient.moveTaskToOtherList(taskId, mPreferences.getDoingList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {},
                           e -> this.onUpdateError(e.getMessage()),
                            () -> {this.startTimer(taskId);});
    }

    private void startTimer(String taskId) {

    }

    private void onUpdateError(String errMessage) {

    }

}
