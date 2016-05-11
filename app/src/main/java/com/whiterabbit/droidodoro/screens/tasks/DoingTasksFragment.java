package com.whiterabbit.droidodoro.screens.tasks;

import com.whiterabbit.droidodoro.storage.ListType;

/**
 * Created by fedepaol on 08/05/16.
 */
public class DoingTasksFragment extends TaskFragment {
    @Override
    ListType getListType() {
        return ListType.DOING;
    }

    @Override
    int getLoaderId() {
        return 1;
    }
}
