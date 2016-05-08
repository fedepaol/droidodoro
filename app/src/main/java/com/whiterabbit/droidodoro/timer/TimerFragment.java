package com.whiterabbit.droidodoro.timer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whiterabbit.droidodoro.R;

public class TimerFragment extends Fragment {

    private static final String TASK_PARAM = "task";

    private String mTaskId;

    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance(String taskId) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(TASK_PARAM, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTaskId = getArguments().getString(TASK_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

}
