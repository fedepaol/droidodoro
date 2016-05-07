package com.whiterabbit.droidodoro.configuration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.whiterabbit.droidodoro.DroidodoroApplication;
import com.whiterabbit.droidodoro.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConfigurationFragment extends Fragment implements ConfigurationView, LoginFragment.LoginResult {
    @BindView(R.id.configuration_login)  Button login;

    @Inject
    ConfigurationPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DroidodoroApplication app = (DroidodoroApplication) getActivity().getApplication();

        DaggerConfigComponent.builder()
                .applicationComponent(app.getComponent())
                .configModule(new ConfigModule(this))
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.configuration, container, false);
        ButterKnife.bind(this, res);
        return res;
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
    }

    @Override
    public void toggleLogin(boolean enable) {
        login.setEnabled(enable);
    }

    @OnClick(R.id.configuration_login)
    public void onLoginClicked() {
        mPresenter.onLoginPressed();
    }

    @Override
    public void toggleListsSpinners(boolean enable) {

    }

    @Override
    public void setBoards(String[] boards) {

    }

    @Override
    public void setTodos(String[] todos) {

    }

    @Override
    public void setDoing(String[] doing) {

    }

    @Override
    public void setDone(String[] done) {

    }

    @Override
    public void showProgress(String message, boolean toggle) {

    }

    @Override
    public void askForToken() {
        LoginFragment l = LoginFragment.newInstance(this);
        l.show(getActivity().getSupportFragmentManager(), "logindialog");
    }

    @Override
    public void onTokenFound(String token) {
        mPresenter.onTokenReceived(token);
    }

    @Override
    public void onLoginError(String message) {
        mPresenter.onTokenError(message);
    }
}
