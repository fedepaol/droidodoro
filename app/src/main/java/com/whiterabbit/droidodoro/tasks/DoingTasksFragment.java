package com.whiterabbit.droidodoro.tasks;

/**
 * Created by fedepaol on 08/05/16.
 */
public class DoingTasksFragment extends TaskFragment {
    @Override
    String getListId() {
        return mPreferences.getDoingList();
    }

    @Override
    int getLoaderId() {
        return 1;
    }
}
