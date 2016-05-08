package com.whiterabbit.droidodoro.tasks;

/**
 * Created by fedepaol on 08/05/16.
 */
public class TodoTasksFragment extends TaskFragment {
    @Override
    String getListId() {
        return mPreferences.getTodoList();
    }

    @Override
    int getLoaderId() {
        return 0;
    }
}
