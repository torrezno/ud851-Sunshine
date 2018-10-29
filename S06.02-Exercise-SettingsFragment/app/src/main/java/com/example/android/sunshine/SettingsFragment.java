package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat
                                implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_screen);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for(int i = 0; i<count; i++){
            Preference p = preferenceScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)) {
                String key = p.getKey();
                String value = sharedPreferences.getString(key, "");
                setPreferenceSummary(p, value);
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if( p != null){
            if( !( p instanceof CheckBoxPreference) ){
                setPreferenceSummary(p,sharedPreferences.getString(key,""));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setPreferenceSummary(Preference p, Object o){
        String newValue = o.toString();
        String key = p.getKey();

        if(p instanceof ListPreference){
            ListPreference lp = (ListPreference) p;
            int i = lp.findIndexOfValue(newValue);
            String label = lp.getEntries()[i].toString();
            p.setSummary(label);
        }else {
            p.setSummary(newValue);
        }
    }
}
