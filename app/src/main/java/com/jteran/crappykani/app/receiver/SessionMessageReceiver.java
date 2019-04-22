package com.jteran.crappykani.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jteran.crappykani.app.service.SessionMessengerService;
import com.jteran.crappykani.models.MessageType;

import io.reactivex.annotations.Nullable;


public class SessionMessageReceiver extends BroadcastReceiver {

    private SessionMessageListener listener;

    public SessionMessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String message = extras.getString(SessionMessengerService.MESSAGE_CONTENT_KEY);
            int type = extras.getInt(SessionMessengerService.MESSAGE_TYPE_KEY);

            if (listener != null)
                listener.onMessageReceived(message, type);
        }
    }

    public void attachListener(SessionMessageListener listener) {
        this.listener = listener;
    }

    public void releaseListener() {
        this.listener = null;
    }

    public interface SessionMessageListener {
        void onMessageReceived(@Nullable String message, @MessageType int type);
    }
}
