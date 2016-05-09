package com.whiterabbit.droidodoro.tasks;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.configuration.ConfigurationActivity;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksActivity extends AppCompatActivity {
    @BindView(R.id.tasks_tabs) TabLayout mTabs;
    @BindView(R.id.tasks_activity_pager) ViewPager mPager;

    @Inject PreferencesUtils mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);
        ButterKnife.bind(this);
        ((DroidodoroApplication) getApplication()).getComponent().inject(this);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mPager.setAdapter(pagerAdapter);
        mTabs.setupWithViewPager(mPager);

        if (mPreferences.didNotAuthenticate()) {
            goToConfiguration();
        }
    }

    private void goToConfiguration() {
        Intent i = new Intent(this, ConfigurationActivity.class);
        startActivity(i);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        public PagerAdapter(FragmentManager fm, Context c) {
            super(fm);
            mContext = c;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TodoTasksFragment();
                case 1:
                    return new DoingTasksFragment();
                case 2:
                    return new DoneTasksFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return mContext.getString(R.string.tasks_todo);
                case 1:
                    return mContext.getString(R.string.tasks_doing);
                case 2:
                    return mContext.getString(R.string.tasks_done);
            }
            return "";
        }
    }


}
