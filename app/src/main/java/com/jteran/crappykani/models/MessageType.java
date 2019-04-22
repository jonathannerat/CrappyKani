package com.jteran.crappykani.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        MessageType.NONE,
        MessageType.INFO,
        MessageType.SUCCESS,
        MessageType.WARNING,
        MessageType.ERROR,
})
@Retention(RetentionPolicy.SOURCE)
public @interface MessageType {
    int NONE = -1;
    int INFO = 0;
    int SUCCESS = 1;
    int WARNING = 2;
    int ERROR = 3;
}
