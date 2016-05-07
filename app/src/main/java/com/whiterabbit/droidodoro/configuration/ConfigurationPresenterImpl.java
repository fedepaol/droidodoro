package com.whiterabbit.droidodoro.configuration;

import android.content.Context;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.model.Board;
import com.whiterabbit.droidodoro.model.TrelloList;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.trelloclient.TrelloClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ConfigurationPresenterImpl implements ConfigurationPresenter {
    /* helper class to be filled with a board and related lists */
    private static class BoardList {
        private Board board;
        private List<TrelloList> lists;

        public BoardList(Board board, List<TrelloList> lists) {
            this.board = board;
            this.lists = lists;
        }

        public Board getBoard() {
            return board;
        }

        public List<TrelloList> getLists() {
            return lists;
        }
    }

    private ConfigurationView mView;
    private PreferencesUtils mPreferences;
    private TrelloClient mTrello;
    private ArrayList<BoardList> mBoards;
    private Subscription mBoardsSubscription;

    public ConfigurationPresenterImpl(ConfigurationView v,
                                      TrelloClient trello,
                                      PreferencesUtils prefUtils) {
        mView = v;
        mPreferences = prefUtils;
        mTrello = trello;
        mBoards = new ArrayList<>();
    }

    private void initNoToken() {
        mView.toggleLogin(true);
        mView.toggleListsSpinners(false);
    }

    private void initToken() {
        mView.toggleLogin(false);
        if (mBoards.size() > 0) { // already have the boards filled
            onBoardsComplete();
            return;
        }
        mView.showProgress(R.string.config_fetching_boards, true);
        mBoardsSubscription = mTrello.getBoards()
                .flatMap(Observable::from)
                .flatMap(b -> mTrello.getLists(b.getId())
                              .map(l -> new BoardList(b, l)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> mBoards.add(b),
                           e -> {
                               this.onTrelloError(e.getMessage());
                               //mView.showProgress(0, false);
                           }, () -> {
                                this.onBoardsComplete();
                                mView.showProgress(0, false);
                            });
    }

    private void onBoardsComplete() {
        String boards[] = new String[mBoards.size()];
        int i = 0;
        for (BoardList b : mBoards) {
            boards[i++] = b.getBoard().getName();
        }
        mView.setBoards(boards);
    }

    private void onTrelloError(String message) {

    }

    @Override
    public void onResume() {
        if (mPreferences.getAuthToken().equals("")) {
            initNoToken();
        } else {
            initToken();
        }
    }

    @Override
    public void onPause() {
        if (mBoardsSubscription != null) {
            mBoardsSubscription.unsubscribe();
        }
    }

    @Override
    public void onTokenReceived(String token) {
        mPreferences.setAuthToken(token);
        initToken();
    }

    @Override
    public void onTokenError(String error) {

    }

    @Override
    public void onLoginPressed() {
        mView.askForToken();
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
