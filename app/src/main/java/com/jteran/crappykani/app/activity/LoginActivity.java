package com.jteran.crappykani.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jteran.crappykani.R;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.credential.LoginCredentials;
import com.jteran.crappykani.wanikani.Wanikani;

import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity implements Wanikani.LoginListener {

    /**
     * creates new bundle with the needed extras to show the alert message
     * <p>
     * It's purpose is to restrict the possible values for the alert type
     *
     * @param message message to be displayed
     * @param type    type of the message (defines the bgcolor)
     * @return a bundle with the extras necessary to  display.
     */
    public static Bundle createAlertMsgBungle(@NonNull String message, @LoginAlertType int type) {
        Bundle bundle = new Bundle();
        bundle.putString("login_alert_msg", message);
        bundle.putInt("login_alert_type", type);
        return bundle;
    }

    /**
     * Alert box for message display
     */
    private TextView loginAlert;
    private ViewSwitcher loadingSwitcher;
    private LoginCredentials loginCredentials;

    /**
     * Disposable for login background operations
     */
    private Disposable loginDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAlert = findViewById(R.id.login_alert);
        loadingSwitcher = findViewById(R.id.loading_switcher);

        String loginAlertMsg = getIntent().getStringExtra("login_alert_msg");

        if (loginAlertMsg != null) {
            int loginAlertType = getIntent().getIntExtra("login_alert_type", 0);
            showAlertMessage(loginAlertMsg, loginAlertType);
        }

        if (PrefManager.isUserLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            final EditText userLogin = findViewById(R.id.user_login);

            String lastUserLoginName = PrefManager.getLastUserLoggedIn();
            Button loginButton = findViewById(R.id.login_button);

            if (lastUserLoginName != null) {
                userLogin.setText(lastUserLoginName);
            }

            loginButton.setOnClickListener(v -> {
                if (loadingSwitcher.getDisplayedChild() == 0) loadingSwitcher.showNext();

                loginCredentials = getLoginCredentials();

                if (loginCredentials != null) {
                    loginDisposable = Wanikani.login(loginCredentials, LoginActivity.this);
                }
            });

        }
    }

    private LoginCredentials getLoginCredentials() {

        try {
            String userLogin = ((TextView) findViewById(R.id.user_login)).getText().toString();
            String userPassword = ((TextView) findViewById(R.id.user_password)).getText().toString();

            return new LoginCredentials(userLogin, userPassword);
        } catch (IllegalArgumentException e) {
            if (loadingSwitcher.getDisplayedChild() == 1) loadingSwitcher.showPrevious();

            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginDisposable != null) loginDisposable.dispose();
    }

    @Override
    public void onLoginSuccess() {
        if (loginDisposable != null) loginDisposable.dispose();
        PrefManager.saveUserCredentials(loginCredentials);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginError(Throwable t) {
        ViewSwitcher loadingSwitcher = findViewById(R.id.loading_switcher);

        showAlertMessage(t.getLocalizedMessage(), LoginAlertType.ERROR);

        if (loadingSwitcher.getDisplayedChild() == 1) {
            loadingSwitcher.showPrevious();
        }
    }


    /**
     * Helper method to show a message box
     *
     * @param message message to be displayed
     * @param type    the type of message (defines the bgcolor)
     */
    private void showAlertMessage(String message, @LoginAlertType int type) {
        @IdRes int alertColors[] = {
                R.color.infoTextBackground,
                R.color.successTextBackground,
                R.color.warningTextBackground,
                R.color.errorTextBackground
        };

        loginAlert.setText(message);
        loginAlert.setBackgroundColor(getResources().getColor(alertColors[type]));
        loginAlert.setVisibility(View.VISIBLE);
    }

    /**
     * Specifies the possible values for the login alert type
     */
    @IntDef({
            LoginAlertType.INFO,
            LoginAlertType.SUCCESS,
            LoginAlertType.WARNING,
            LoginAlertType.ERROR,
    })

    public @interface LoginAlertType {
        int INFO = 0;
        int SUCCESS = 1;
        int WARNING = 2;
        int ERROR = 3;
    }
}
