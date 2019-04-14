package com.jteran.crappykani.app.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.jteran.crappykani.app.mvp.view.LoginView;
import com.jteran.crappykani.app.receiver.LoginStatusChangeReceiver;
import com.jteran.crappykani.app.service.LoginService;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.MessageType;
import com.jteran.crappykani.models.credential.LoginCredentials;

public class LoginPresenter extends MvpBasePresenter<LoginView>
        implements LoginStatusChangeReceiver.LoginStatusChangeListener {

    /**
     * Starts login service using context with given credentials
     *
     * @param context  to start the service
     * @param username login credential username
     * @param password login credential password
     */
    public void login(Context context, String username, String password) {
        LoginCredentials credentials = null;

        ifViewAttached(view -> view.triggerLoading(true));

        try {
            credentials = new LoginCredentials(username, password);
        } catch (IllegalArgumentException e) {
            ifViewAttached(view -> {
                view.triggerLoading(false);
                view.showLoginResult(e.getLocalizedMessage(), MessageType.ERROR);
            });
        }

        if (credentials != null) {
            Intent loginService = new Intent(context, LoginService.class);

            loginService.putExtra(LoginService.LOGIN_USERNAME_KEY, credentials.getUsername());
            loginService.putExtra(LoginService.LOGIN_PASSWORD_KEY, credentials.getPassword());

            context.startService(loginService);
        }
    }

    @Override
    public void onLoginStatusChange(@LoginStatus int status, @Nullable String errorMsg) {
        if (status == LoginStatus.LOGGED_IN) {
            ifViewAttached(view -> {
                view.showLoginResult("Logged in successfully!", MessageType.SUCCESS);
                view.proceedToDashboard();
            });
        } else {
            ifViewAttached(view -> {
                view.triggerLoading(false);
                view.showLoginResult(errorMsg, MessageType.ERROR);
            });
        }
    }


}
