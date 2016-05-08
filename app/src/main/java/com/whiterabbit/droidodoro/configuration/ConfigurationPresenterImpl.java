package com.whiterabbit.droidodoro.configuration;

import android.content.Context;

import com.whiterabbit.droidodoro.R;
import com.whiterabbit.droidodoro.model.Board;
import com.whiterabbit.droidodoro.model.Card;
import com.whiterabbit.droidodoro.model.TrelloList;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;
import com.whiterabbit.droidodoro.storage.TaskProviderClientExt;
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
    private TaskProviderClientExt mProviderClient;

    public ConfigurationPresenterImpl(ConfigurationView v,
                                      TrelloClient trello,
                                      PreferencesUtils prefUtils,
                                      TaskProviderClientExt providerClient) {
        mView = v;
        mPreferences = prefUtils;
        mTrello = trello;
        mBoards = new ArrayList<>();
        mProviderClient = providerClient;
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
                               mView.showProgress(0, false);
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
        updateListsFromBoard();
    }

    private void updateListsFromBoard() {
        BoardList b = mBoards.get(mView.getBoardPosition());
        List<TrelloList> l = b.getLists();
        String[] lists = new String[l.size()];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = l.get(i).getName();
        }
        mView.setTodos(lists);
        mView.setDoing(lists);
        mView.setDone(lists);
    }

    @Override
    public void onBoardUpdated() {
        updateListsFromBoard();
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
    public void onImportPressed() {
        int todoIndx = mView.getTodoPosition();
        int doingIndx = mView.getDoingPosition();
        int doneIndx = mView.getDonePosition();

        if (todoIndx == doingIndx ||
            todoIndx == doneIndx ||
            doingIndx == doneIndx) {
            mView.notifyError(R.string.config_different_lists);
        }
        int boardIndx = mView.getBoardPosition();
        BoardList board = mBoards.get(boardIndx);
        String todoId = board.getLists().get(todoIndx).getId();
        String doingId = board.getLists().get(doingIndx).getId();
        String doneId = board.getLists().get(doneIndx).getId();

        /* Chain:
            - deletes from the storage
            - fetches the cards from each list
            - stores the cards
            - stores the ids of the three lists
            Reilies on the fact that map is performed in the subscription thread
         */
        Observable<Integer> o = Observable.fromCallable(mProviderClient::removeAllTask);
        o.flatMap(i -> mTrello.getCards(todoId))
                .map(t -> this.storeCards(t, todoId))
                .flatMap(b -> mTrello.getCards(doingId))
                .map(d ->  this.storeCards(d, doingId))
                .flatMap(b -> mTrello.getCards(doneId))
                .map(done ->  this.storeCards(done, doneId))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    mPreferences.setTodoList(todoId);
                    mPreferences.setDoingList(doingId);
                    mPreferences.setDoneList(doneId);
                    this.onSetupComplete();
                }, e -> {
                    this.onTrelloError(e.getMessage());
                    mView.showProgress(0, false);
                    },
                    () -> mView.showProgress(0, false));
    }

    private boolean storeCards(List<Card> cards, String listId) {
        mProviderClient.addCards(cards, listId) ;
        return true;
    }

    private void onSetupComplete() {

    }
}
