package com.jteran.crappykani.app.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jteran.crappykani.R;
import com.jteran.crappykani.app.mvp.presenter.LoginPresenter;
import com.jteran.crappykani.app.mvp.view.LoginView;
import com.jteran.crappykani.app.receiver.SessionMessageReceiver;
import com.jteran.crappykani.app.service.LoginService;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.MessageType;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter>
        implements LoginView, SessionMessageReceiver.SessionMessageListener {

    public static final String LOGIN_MESSAGE_CONTENT_KEY = "com.jteran.crappykani.login.MESSAGE_CONTENT";
    public static final String LOGIN_MESSAGE_TYPE_KEY = "com.jteran.crappykani.login.MESSAGE_TYPE";

    private final SessionMessageReceiver sessionMessageReceiver = new SessionMessageReceiver();
    private final IntentFilter sessionMessageFilter = new IntentFilter(LoginService.SESSION_MESSAGE_SENT_ACTION);
    private final int[] alertColors = {
            R.color.infoTextBackground,
            R.color.successTextBackground,
            R.color.warningTextBackground,
            R.color.errorTextBackground
    };

    private Button loginBtn;
    private TextView usernameTxt;
    private TextView passwordTxt;
    private ViewSwitcher viewSwitcher;
    private TextView loginAlert;

    private void viewBindings() {
        loginBtn = findViewById(R.id.login_button);
        usernameTxt = findViewById(R.id.user_login);
        passwordTxt = findViewById(R.id.user_password);
        loginAlert = findViewById(R.id.login_alert);
        viewSwitcher = findViewById(R.id.loading_switcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewBindings();
        presenter.handleIntent(getIntent());
        loginBtn.setOnClickListener(this::handleLogin);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PrefManager.isUserLoggedIn()) {
            proceedToDashboard();
        } else {
            // Register login status receiver
            sessionMessageReceiver.attachListener(this);
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(sessionMessageReceiver, sessionMessageFilter);
        }
    }

    @Override
    protected void onStop() {

        sessionMessageReceiver.releaseListener();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sessionMessageReceiver);
        super.onStop();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void triggerLoading(boolean showLoading) {
        if (showLoading && viewSwitcher.getDisplayedChild() == 0) {
            viewSwitcher.showNext();
        } else if (!showLoading && viewSwitcher.getDisplayedChild() == 1) {
            viewSwitcher.showPrevious();
        }
    }

    @Override
    public void showLoginResult(String message, @MessageType int type) {
        loginAlert.setText(message);
        loginAlert.setBackgroundColor(getResources().getColor(alertColors[type]));

        if (loginAlert.getVisibility() == View.GONE)
            loginAlert.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleLogin(View v) {
        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        presenter.login(this, username, password);
    }

    @Override
    public void proceedToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);

        startActivity(dashboardIntent);
        finish();
    }

    @Override
    public void onMessageReceived(@Nullable String message, int type) {
        presenter.onMessageReceived(message, type);
    }
}
