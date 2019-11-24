package com.michaelfotiadis.ibeaconscanner.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.michaelfotiadis.ibeaconscanner.R;
import com.michaelfotiadis.ibeaconscanner.utils.log.AppLog;

public abstract class BaseActivity extends AppCompatActivity {
    protected static final int NO_LAYOUT = Integer.MIN_VALUE;
    private Toolbar mToolbar;

    public abstract int getLayoutResource();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AppLog.d("OnCreate");
        if (getLayoutResource() != NO_LAYOUT) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("On Create with layout resource ");
            stringBuilder.append(getLayoutResource());
            AppLog.d(stringBuilder.toString());
            setContentView(getLayoutResource());
            setupActionBar();
        }
    }

    public Toolbar getToolbar() {
        return this.mToolbar;
    }

    /* Access modifiers changed, original: protected */
    public void setupActionBar() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = this.mToolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName());
        stringBuilder.append(": Null toolbar");
        AppLog.w(stringBuilder.toString());
    }

    public void setDisplayHomeAsUpEnabled(boolean z) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(z);
        }
    }

    /* Access modifiers changed, original: protected */
    public void addContentFragmentIfMissing(Fragment fragment, int i, String str) {
        if (getSupportFragmentManager().findFragmentByTag(str) == null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.add(i, fragment, str);
            beginTransaction.commit();
        }
    }
}
