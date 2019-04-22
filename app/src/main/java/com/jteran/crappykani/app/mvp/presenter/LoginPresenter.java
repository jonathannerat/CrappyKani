package com.jteran.crappykani.app.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.jteran.crappykani.app.activity.LoginActivity;
import com.jteran.crappykani.app.mvp.view.LoginView;
import com.jteran.crappykani.app.receiver.SessionMessageReceiver;
import com.jteran.crappykani.app.service.LoginService;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.MessageType;

public class LoginPresenter extends MvpBasePresenter<LoginView>
        implements SessionMessageReceiver.SessionMessageListener {

    /**
     * Starts login service using context with given credentials
     *
     * @param context  to start the service
     * @param username login credential username
     * @param password login credential password
     */
    public void login(@NonNull Context context, @NonNull String username, @NonNull String password) {

        ifViewAttached(view -> view.triggerLoading(true));

        Intent loginIntent = new Intent(context, LoginService.class)
                .putExtra(LoginService.LOGIN_USERNAME_KEY, username)
                .putExtra(LoginService.LOGIN_PASSWORD_KEY, password);

        context.startService(loginIntent);
    }

    @Override
    public void onMessageReceived(@Nullable String message, @MessageType int type) {
        ifViewAttached(view -> {
            if (PrefManager.isUserLoggedIn()) {
                view.proceedToDashboard();
            } else {
                if (message != null) {
                    view.showLoginResult(message, type);
                }

                view.triggerLoading(false);
            }
        });
    }


    public void handleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String message = extras.getString(LoginActivity.LOGIN_MESSAGE_CONTENT_KEY);
            int type = extras.getInt(LoginActivity.LOGIN_MESSAGE_TYPE_KEY);

            onMessageReceived(message, type);
        }
    }
}
