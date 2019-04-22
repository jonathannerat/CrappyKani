package com.jteran.crappykani.app.mvp.view;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.jteran.crappykani.models.MessageType;

public interface BaseView extends MvpView {
    void closeDrawer();

    void returnToLogin(@Nullable String message, @MessageType int type);
}
