package com.whiterabbit.droidodoro.configuration;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConfigurationFragment extends Fragment implements ConfigurationView,
                                                               LoginFragment.LoginResult,
                                                               AdapterView.OnItemSelectedListener {
    @BindView(R.id.configuration_login)  Button mLogin;
    @BindView(R.id.configuration_import) Button mImport;
    @BindView(R.id.configuration_boards) Spinner mBoardsSpinner;
    @BindView(R.id.configuration_todo) Spinner mTodoSpinner;
    @BindView(R.id.configuration_doing) Spinner mDoingSpinner;
    @BindView(R.id.configuration_done) Spinner mDoneSpinner;


    @Inject
    ConfigurationPresenter mPresenter;
    @Inject
    Context mContext;

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DroidodoroApplication app = (DroidodoroApplication) getActivity().getApplication();

        DaggerConfigComponent.builder()
                .applicationComponent(app.getComponent())
                .configModule(new ConfigModule(this))
                .build().inject(this);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.configuration, container, false);
        ButterKnife.bind(this, res);
        mBoardsSpinner.setOnItemSelectedListener(this);
        return res;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void toggleLogin(boolean enable) {
        mLogin.setEnabled(enable);
    }

    @OnClick(R.id.configuration_login)
    public void onLoginClicked() {
        mPresenter.onLoginPressed();
    }

    @Override
    public void toggleListsSpinners(boolean enable) {

    }

    @Override
    public void toggleImport(boolean enable) {
        mImport.setEnabled(enable);
    }

    private void setSpinnerValues(Spinner s, String[] values) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                R.layout.config_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void setBoards(String[] boards) {
        setSpinnerValues(mBoardsSpinner, boards);
    }

    @Override
    public void setTodos(String[] todos) {
        setSpinnerValues(mTodoSpinner, todos);
    }

    @Override
    public void setDoing(String[] doing) {
        setSpinnerValues(mDoingSpinner, doing);
    }

    @Override
    public void setDone(String[] done) {
        setSpinnerValues(mDoneSpinner, done);
    }

    @Override
    public void showProgress(int message, boolean toggle) {
        if (!toggle) {
            mProgressDialog.dismiss();
            return;
        }
        String m = getString(message);
        mProgressDialog.setMessage(m);
        mProgressDialog.show();
    }

    @Override
    public void askForToken() {
        LoginFragment l = LoginFragment.newInstance(this);
        l.show(getActivity().getSupportFragmentManager(), "logindialog");
    }

    @Override
    public int getBoardPosition() {
        return mBoardsSpinner.getSelectedItemPosition();
    }

    @Override
    public int getTodoPosition() {
        return mTodoSpinner.getSelectedItemPosition();
    }

    @Override
    public int getDoingPosition() {
        return mDoingSpinner.getSelectedItemPosition();
    }

    @Override
    public int getDonePosition() {
        return mDoneSpinner.getSelectedItemPosition();
    }

    @Override
    public void onTokenFound(String token) {
        mPresenter.onTokenReceived(token);
    }

    @Override
    public void onLoginError(String message) {
        mPresenter.onTokenError(message);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onBoardUpdated();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.configuration_import)
    public void onImportPressed(View v) {
        mPresenter.onImportPressed();
    }

    @Override
    public void notifyError(int stringId) {
        notifyError(getString(stringId));
    }

    @Override
    public void notifyError(String message) {
        Snackbar.make(getView(),
                message, Snackbar.LENGTH_LONG).show();
    }
}
