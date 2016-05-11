package com.whiterabbit.droidodoro.screens.tasks;

import com.whiterabbit.droidodoro.storage.ListType;

/**
 * Created by fedepaol on 08/05/16.
 */
public class DoneTasksFragment extends TaskFragment {
    @Override
    ListType getListType() {
        return ListType.DONE;
    }

    @Override
    int getLoaderId() {
        return 2;
    }
}
