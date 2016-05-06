package com.whiterabbit.droidodoro.configuration;

/**
 * Created by fedepaol on 06/05/16.
 */
public interface ConfigurationView {
    void toggleLogin(boolean enable);

    void toggleListsSpinners(boolean enable);

    void toggleBoardsSpinners(boolean enable);

    void setBoards(String[] boards);

    void setTodos(String[] todos);

    void setDoing(String[] doing);

    void setDone(String[] done);

    void showProgress(String message);

    void askForToken();
}
