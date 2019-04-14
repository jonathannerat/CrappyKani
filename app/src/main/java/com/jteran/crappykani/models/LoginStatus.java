package com.jteran.crappykani.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        LoginStatus.LOGGED_OUT,
        LoginStatus.LOGGED_IN,
        LoginStatus.PAT_FETCHED
})
@Retention(RetentionPolicy.SOURCE)
public @interface LoginStatus {
    int LOGGED_OUT = 0;
    int LOGGED_IN = 1;
    int PAT_FETCHED = 2;
}
