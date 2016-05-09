package com.whiterabbit.droidodoro.timer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimerFragment extends Fragment implements TimerView, TimerActivity.OnBackPressedListener {
    @Inject TimerPresenter mPresenter;

    @BindView(R.id.timer_to_start_controls) View mToStartControls;
    @BindView(R.id.timer_break_controls) View mBreakControls;
    @BindView(R.id.timer_finished_controls) View mFinishedControls;
    @BindView(R.id.timer_ongoing_controls) View mOngoingControls;
    @BindView(R.id.timer_task_name) TextView mTaskName;
    @BindView(R.id.timer_tot_time) TextView mTotTimeSpent;
    @BindView(R.id.timer_tot_pomodoros) TextView mTotPomodoros;
    @BindView(R.id.timer_current_time) TextView mCurrentTime;

    @BindView(R.id.timer_pause_restart_button) Button mPauseRestart;

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

        DroidodoroApplication app = (DroidodoroApplication) getActivity().getApplication();

        DaggerTimerComponent.builder()
                .applicationComponent(app.getComponent())
                .timerModule(new TimerModule(this))
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View res = inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this, res);
        return res;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((TimerActivity) getActivity()).setOnBackListener(this);
    }

    @Override
    public String getTaskId() {
        return mTaskId;
    }

    @Override
    public void setTaskValues(String title, int pomodoros, long totalTime) {
        mTaskName.setText(title);
        mTotPomodoros.setText(String.valueOf(pomodoros));
        mTotTimeSpent.setText(Utils.getTimeFromSeconds(totalTime));
    }

    @Override
    public void toggleToStartControls(boolean show) {
        if (show)
            mToStartControls.setVisibility(View.VISIBLE);
        else
            mToStartControls.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toggleTimerGoingControls(boolean show) {
        if (show)
            mOngoingControls.setVisibility(View.VISIBLE);
        else
            mOngoingControls.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toggleTimerFinishedControls(boolean show) {
        if (show)
            mFinishedControls.setVisibility(View.VISIBLE);
        else
            mFinishedControls.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toggleBreakControls(boolean show) {
        if (show)
            mBreakControls.setVisibility(View.VISIBLE);
        else
            mBreakControls.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setPauseButtonText(int resId) {
        mPauseRestart.setText(getString(resId));
    }

    @Override
    public void setCurrentTime(long secondsToGo) {
        mCurrentTime.setText(Utils.getTimeFromSeconds(secondsToGo));
    }

    @OnClick(R.id.timer_start_button)
    public void onTimerStart() {
        mPresenter.onStartPressed();
    }

    @OnClick(R.id.timer_stop_button)
    public void onTimerStop() {
        mPresenter.onStopPressed();
    }

    @OnClick(R.id.timer_pause_restart_button)
    public void onPauseRestart() {
        mPresenter.onPauseRestartPressed();
    }

    @OnClick(R.id.timer_short_break_button)
    public void onShortBreak() {
        mPresenter.onShortBreakPressed();
    }

    @OnClick(R.id.timer_long_break_button)
    public void onLongBreak() {
        mPresenter.onLongBreakPressed();
    }

    @OnClick(R.id.timer_end_break_button)
    public void onEndBreak() {
        mPresenter.onStopBreakPressed();
    }

    @OnClick(R.id.timer_done_button)
    public void onDone() {
        mPresenter.onDonePressed();
    }

    @Override
    public void doBack() {
        mPresenter.onBackPressed();
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }
}
