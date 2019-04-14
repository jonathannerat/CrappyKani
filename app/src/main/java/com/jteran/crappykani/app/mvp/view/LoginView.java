package com.jteran.crappykani.app.mvp.view;

import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.jteran.crappykani.models.MessageType;

public interface LoginView extends MvpView {

    void triggerLoading(boolean showLoading);

    void showLoginResult(String message, @MessageType int type);

    void handleLogin(View v);

    void proceedToDashboard();
}
