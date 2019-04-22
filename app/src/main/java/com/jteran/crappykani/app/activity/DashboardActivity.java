package com.jteran.crappykani.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jteran.crappykani.R;
import com.jteran.crappykani.app.mvp.presenter.DashboardPresenter;
import com.jteran.crappykani.app.mvp.view.DashboardView;
import com.jteran.crappykani.app.receiver.SessionMessageReceiver;

public class DashboardActivity extends BaseActivity<DashboardView, DashboardPresenter>
        implements DashboardView, SessionMessageReceiver.SessionMessageListener {

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    public DashboardPresenter createPresenter() {
        return new DashboardPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
