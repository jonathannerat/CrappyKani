package com.jteran.crappykani.app.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    /**
     * Specifies the contentView layout file for the activity
     *
     * @return the layout resource to be used
     */
    protected abstract @LayoutRes
    int layout();

    /**
     * View bindings that should be done right after specifying the contentView
     */
    protected abstract void viewBindings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        viewBindings();
    }
}
