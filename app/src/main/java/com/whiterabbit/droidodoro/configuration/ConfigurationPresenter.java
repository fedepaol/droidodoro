package com.whiterabbit.droidodoro.configuration;

/**
 * Created by fedepaol on 07/05/16.
 */
public interface ConfigurationPresenter {
    void onTokenReceived(String token);
    void onTokenError(String error);
    void onLoginPressed();
    void onTodoSelected(int position);
    void onDoingSelected(int position);
    void onDoneSelected(int position);
}
