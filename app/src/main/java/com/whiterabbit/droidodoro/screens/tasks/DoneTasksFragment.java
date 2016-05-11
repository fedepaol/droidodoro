package com.whiterabbit.droidodoro.screens.tasks;

/**
 * Created by fedepaol on 08/05/16.
 */
public class DoneTasksFragment extends TaskFragment {
    @Override
    String getListId() {
        return mPreferences.getDoneList();
    }

    @Override
    int getLoaderId() {
        return 2;
    }
}
