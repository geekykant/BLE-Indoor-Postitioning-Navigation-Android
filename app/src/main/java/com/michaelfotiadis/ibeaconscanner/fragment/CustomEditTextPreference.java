package com.michaelfotiadis.ibeaconscanner.fragment;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.util.AttributeSet;

public class CustomEditTextPreference extends EditTextPreference {
    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomEditTextPreference(Context context) {
        super(context);
    }
}
