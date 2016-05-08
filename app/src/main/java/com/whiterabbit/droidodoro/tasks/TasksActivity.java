package com.whiterabbit.droidodoro.tasks;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whiterabbit.droidodoro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksActivity extends AppCompatActivity {
    @BindView(R.id.tasks_tabs) TabLayout mTabs;
    @BindView(R.id.tasks_activity_pager) ViewPager mPager;
    private PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        public PagerAdapter(FragmentManager fm, Context c) {
            super(fm);
            mContext = c;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return mContext.getString(R.string.tasks_todo);
            }
            return "";
        }
    }


}
