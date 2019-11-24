package com.michaelfotiadis.ibeaconscanner.activities;

import android.os.Bundle;

import com.michaelfotiadis.ibeaconscanner.R;
import com.michaelfotiadis.ibeaconscanner.fragment.SettingsFragment;

public class ScanPreferencesActivity extends BaseActivity {
    private static final String FRAGMENT_TAG;

    /* Access modifiers changed, original: protected */
    public int getLayoutResource() {
        return R.layout.activity_default_fragment_container;
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ScanPreferencesActivity.class.getSimpleName());
        stringBuilder.append("_fragment_tag");
        FRAGMENT_TAG = stringBuilder.toString();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle(getString(R.string.action_settings));
        setDisplayHomeAsUpEnabled(true);
        addContentFragmentIfMissing(new SettingsFragment(), R.id.content_frame, FRAGMENT_TAG);
    }
}
