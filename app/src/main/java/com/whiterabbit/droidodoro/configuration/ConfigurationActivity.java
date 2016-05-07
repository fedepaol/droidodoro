package com.whiterabbit.droidodoro.configuration;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whiterabbit.droidodoro.R;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        if (getFragmentManager().findFragmentById(R.id.config_fragment) == null) {
            Fragment f = new ConfigurationFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.config_fragment, f)
                    .commit();
        }
    }
}
