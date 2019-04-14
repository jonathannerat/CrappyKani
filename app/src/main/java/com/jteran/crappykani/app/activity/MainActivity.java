package com.jteran.crappykani.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.jteran.crappykani.R;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.wanikani.Wanikani;

public class MainActivity extends AppCompatActivity implements Wanikani.LogoutListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PrefManager.getLoginStatus() == LoginStatus.LOGGED_OUT) {
            redirectToLogin();
        } else {
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionbar = getSupportActionBar();

            if (actionbar != null) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            String apiKey = PrefManager.getPAT();

            if (apiKey != null) {
                TextView apiKeyView = findViewById(R.id.api_key_view);
                apiKeyView.setText(apiKey);
            }

            Button logout = findViewById(R.id.logout_button);

//            logout.setOnClickListener(v -> logoutDisposable = Wanikani.logout(MainActivity.this));
        }
    }

    @Override
    public void onLogoutSuccess() {
        PrefManager.clearUserCredentials();
        redirectToLogin();
    }

    @Override
    public void onLogoutError(Throwable t) {
        PrefManager.clearUserCredentials();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
