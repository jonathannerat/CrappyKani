package com.jteran.crappykani.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.jteran.crappykani.models.MessageType;

public abstract class SessionMessengerService extends IntentService {
    public static final String SESSION_MESSAGE_SENT_ACTION = "com.jteran.crappykani.session.SESSION_MESSAGE_SENT";
    public static final String MESSAGE_CONTENT_KEY = "message_content";
    public static final String MESSAGE_TYPE_KEY = "message_type";

    protected Bundle messageBundle;

    protected SessionMessengerService(String name) {
        super(name);
    }

    protected void sendMessage() {
        Intent intent = new Intent(SESSION_MESSAGE_SENT_ACTION);
        if (messageBundle != null)
            intent.putExtras(messageBundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    protected void createMessageBundle(@Nullable String message, @MessageType int type) {
        messageBundle = new Bundle();
        if (message != null) {
            messageBundle.putString(MESSAGE_CONTENT_KEY, message);
        }
        messageBundle.putInt(MESSAGE_TYPE_KEY, type);
    }


}
