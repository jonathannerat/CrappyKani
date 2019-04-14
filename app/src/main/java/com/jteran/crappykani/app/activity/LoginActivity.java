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
import com.jteran.crappykani.app.receiver.LoginStatusChangeReceiver;
import com.jteran.crappykani.app.service.LoginService;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.MessageType;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter>
        implements LoginView, LoginStatusChangeReceiver.LoginStatusChangeListener {

    private Button loginBtn;
    private TextView usernameTxt;
    private TextView passwordTxt;
    private ViewSwitcher viewSwitcher;
    private LoginStatusChangeReceiver loginStatusChangeReceiver = new LoginStatusChangeReceiver();
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

        if (PrefManager.getLoginStatus() == LoginStatus.LOGGED_IN) {
            proceedToDashboard();
        } else {
            viewBindings();

            loginBtn.setOnClickListener(this::handleLogin);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Register login status receiver
        loginStatusChangeReceiver.attachListener(this);
        IntentFilter loginStatusChangeFilter = new IntentFilter(LoginService.LOGIN_STATUS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                loginStatusChangeReceiver,
                loginStatusChangeFilter
        );

    }

    @Override
    protected void onStop() {
        super.onStop();

        loginStatusChangeReceiver.releaseListener();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginStatusChangeReceiver);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void triggerLoading(boolean showLoading) {
        if (showLoading) {
            viewSwitcher.showNext();
        } else {
            viewSwitcher.showPrevious();
        }
    }

    @Override
    public void showLoginResult(String message, @MessageType int type) {
        int alertColors[] = {
                R.color.infoTextBackground,
                R.color.successTextBackground,
                R.color.warningTextBackground,
                R.color.errorTextBackground
        };

        loginAlert.setText(message);
        loginAlert.setBackgroundColor(getResources().getColor(alertColors[type]));
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
        Intent dashboardIntent = new Intent(this, MainActivity.class);

        startActivity(dashboardIntent);
        finish();
    }

    @Override
    public void onLoginStatusChange(@LoginStatus int status, @Nullable String errorMsg) {
        presenter.onLoginStatusChange(status, errorMsg);
    }
}
