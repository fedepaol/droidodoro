package com.whiterabbit.droidodoro.configuration;

import com.whiterabbit.droidodoro.storage.PreferencesUtils;

/**
 * Created by fedepaol on 07/05/16.
 */
public class ConfigurationPresenterImpl implements ConfigurationPresenter {
    private ConfigurationView mView;
    private PreferencesUtils mPreferences;

    public ConfigurationPresenterImpl(ConfigurationView v, PreferencesUtils prefUtils) {
        mView = v;
        mPreferences = prefUtils;
        if (mPreferences.getAuthToken().equals("")) {
            mView.toggleLogin(true);

            mView.askForToken();
        }
    }

    @Override
    public void onTokenReceived(String token) {
        mPreferences.setAuthToken(token);

    }

    @Override
    public void onTokenError(String error) {

    }

    @Override
    public void onLoginPressed() {

    }

    @Override
    public void onTodoSelected(int position) {

    }

    @Override
    public void onDoingSelected(int position) {

    }

    @Override
    public void onDoneSelected(int position) {

    }
}
