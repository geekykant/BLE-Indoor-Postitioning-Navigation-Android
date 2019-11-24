package com.michaelfotiadis.ibeaconscanner.fragment;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.EditText;

public class EditTextPreferenceDialog extends EditTextPreferenceDialogFragmentCompat {
    public static EditTextPreferenceDialog newInstance(String str) {
        EditTextPreferenceDialog editTextPreferenceDialog = new EditTextPreferenceDialog();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        editTextPreferenceDialog.setArguments(bundle);
        return editTextPreferenceDialog;
    }

    /* Access modifiers changed, original: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        ((EditText) view.findViewById(16908291)).setInputType(2);
    }
}
