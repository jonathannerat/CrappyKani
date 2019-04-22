package com.jteran.crappykani.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CheckLoginReceiver extends BroadcastReceiver {
    private CheckLoginListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void releaseListener() {
        listener = null;
    }

    public void attachListener(CheckLoginListener listener) {
        this.listener = listener;
    }

    interface CheckLoginListener {
        void onCheckLogin(boolean userIsLoggedIn);
    }
}
