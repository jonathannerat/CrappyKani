package com.jteran.crappykani.app;

import android.app.Application;

import com.jteran.crappykani.BuildConfig;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        PrefManager.initialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
