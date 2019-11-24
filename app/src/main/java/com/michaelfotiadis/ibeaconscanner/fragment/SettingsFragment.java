package com.michaelfotiadis.ibeaconscanner.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;

import com.michaelfotiadis.ibeaconscanner.R;

public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {
    private static final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";

    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.prefs, str);
        initSummary(getPreferenceScreen());
    }

    private void initSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                initSummary(preferenceGroup.getPreference(i));
            }
            return;
        }
        updatePrefSummary(preference);
    }

    public void onDisplayPreferenceDialog(Preference preference) {
        FragmentManager fragmentManager = getFragmentManager();
        String str = DIALOG_FRAGMENT_TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            DialogFragment dialogFragment = null;
            if (preference instanceof CustomEditTextPreference) {
                dialogFragment = EditTextPreferenceDialog.newInstance(preference.getKey());
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), str);
            }
        }
    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        updatePrefSummary(findPreference(str));
    }

    private void updatePrefSummary(Preference preference) {
        if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry());
        }
        if (preference instanceof EditTextPreference) {
            preference.setSummary(((EditTextPreference) preference).getText());
        }
    }
}
