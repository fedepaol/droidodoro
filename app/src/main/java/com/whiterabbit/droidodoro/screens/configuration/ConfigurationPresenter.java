package com.whiterabbit.droidodoro.screens.configuration;

/**
 * Created by fedepaol on 07/05/16.
 */
public interface ConfigurationPresenter {
    void onTokenReceived(String token);
    void onTokenError(String error);
    void onLoginPressed();
    void onPause();
    void onResume();
    void onBoardUpdated();
    void onImportPressed();
}
