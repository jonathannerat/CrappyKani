package com.jteran.crappykani.app.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jteran.crappykani.R;
import com.jteran.crappykani.app.mvp.presenter.BasePresenter;
import com.jteran.crappykani.app.mvp.view.BaseView;
import com.jteran.crappykani.app.receiver.SessionMessageReceiver;
import com.jteran.crappykani.app.service.CheckLoginService;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.MessageType;

public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>>
        extends MvpActivity<V, P>
        implements BaseView,
        NavigationView.OnNavigationItemSelectedListener,
        SessionMessageReceiver.SessionMessageListener {

    private final SessionMessageReceiver sessionMessageReceiver = new SessionMessageReceiver();
    private final IntentFilter sessionMessageFilter = new IntentFilter(CheckLoginService.SESSION_MESSAGE_SENT_ACTION);

    private Toolbar toolbar;

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    private void setupSupportActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Specifies the contentView layout file for the activity
     *
     * @return the layout resource to be used
     */
    @LayoutRes
    protected abstract int layout();

    /**
     * View bindings that should be done right after specifying the contentView
     */
    @CallSuper
    protected void viewBindings() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());

        if (!PrefManager.wasCheckLoginCalled() && !PrefManager.isUserLoggedIn()) {
            presenter.checkIfUserLoggedIn(this);
        }

        viewBindings();
        setupSupportActionBar();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sessionMessageReceiver.attachListener(this);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(sessionMessageReceiver, sessionMessageFilter);
    }

    @Override
    protected void onStop() {
        sessionMessageReceiver.releaseListener();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sessionMessageReceiver);

        super.onStop();
    }

    @Override
    public void closeDrawer() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void returnToLogin(@Nullable String message, @MessageType int type) {
        Intent loginIntent = new Intent(this, LoginActivity.class)
                .putExtra(LoginActivity.LOGIN_MESSAGE_CONTENT_KEY, message)
                .putExtra(LoginActivity.LOGIN_MESSAGE_TYPE_KEY, type);

        startActivity(loginIntent);
        finish();
    }

    private void returnToLogin() {
        returnToLogin(null, MessageType.NONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isChecked()) return false;

        int itemId = item.getItemId();

        if (itemId == R.id.menu_logout) {
            // TODO: add confirmation dialog and loading on confirmation
            presenter.logout(this);
        }

        return true;
    }

    @Override
    public void onMessageReceived(String message, int type) {
        presenter.onMessageReceived(message, type);
    }
}
