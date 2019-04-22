package com.jteran.crappykani.app.mvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.jteran.crappykani.app.mvp.view.BaseView;
import com.jteran.crappykani.app.receiver.SessionMessageReceiver;
import com.jteran.crappykani.app.service.CheckLoginService;
import com.jteran.crappykani.app.service.LogoutService;
import com.jteran.crappykani.manager.preferences.PrefManager;

import timber.log.Timber;


public class BasePresenter<V extends BaseView> extends MvpBasePresenter<V>
        implements SessionMessageReceiver.SessionMessageListener {

    public void logout(Context context) {
        context.startService(new Intent(context, LogoutService.class));
    }

    public void checkIfUserLoggedIn(Context context) {
        Intent checkLoginIntent = new Intent(context, CheckLoginService.class);

        context.startService(checkLoginIntent);
        Timber.d("check login called: %s", PrefManager.wasCheckLoginCalled());
    }

    @Override
    public void onMessageReceived(String message, int type) {
        ifViewAttached(view -> {
            if (!PrefManager.isUserLoggedIn()) {
                view.returnToLogin(message, type);
            }
        });
    }
}
