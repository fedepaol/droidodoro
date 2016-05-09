package com.whiterabbit.droidodoro.timer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.configuration.ConfigurationFragment;

import butterknife.OnClick;

public class TimerActivity extends AppCompatActivity {
    public interface OnBackPressedListener {
        public void doBack();
    }

    public static final String TASK_PARAM = "com.whiterabbit.task";
    private OnBackPressedListener mOnBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        String taskName = getIntent().getStringExtra(TASK_PARAM);
        if (getFragmentManager().findFragmentById(R.id.timer_fragment) == null) {
            Fragment f = TimerFragment.newInstance(taskName);
            getSupportFragmentManager().beginTransaction().add(R.id.timer_fragment, f)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mOnBackListener != null) {
            mOnBackListener.doBack();
        }
        super.onBackPressed();
    }

    public void setOnBackListener(OnBackPressedListener onBackListener) {
        this.mOnBackListener = onBackListener;
    }
}
