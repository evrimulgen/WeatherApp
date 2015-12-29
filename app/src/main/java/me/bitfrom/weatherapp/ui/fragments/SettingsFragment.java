package me.bitfrom.weatherapp.ui.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.bitfrom.weatherapp.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    private SwitchPreference mainSwitch;
    private SwitchPreference vibSwitch;
    private SwitchPreference ledSwitch;
    private SwitchPreference soundSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        mainSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_notifications_key));
        vibSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_vibration_key));
        ledSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_led_key));
        soundSwitch = (SwitchPreference) findPreference(getString(R.string.pref_enable_sound_key));

        vibSwitch.setEnabled(mainSwitch.isChecked());
        ledSwitch.setEnabled(mainSwitch.isChecked());
        soundSwitch.setEnabled(mainSwitch.isChecked());

        mainSwitch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                vibSwitch.setEnabled(mainSwitch.isChecked());
                ledSwitch.setEnabled(mainSwitch.isChecked());
                soundSwitch.setEnabled(mainSwitch.isChecked());
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_frequency_of_updates_key)));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();


        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }


    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        // Trigger the listener immediately with the preference's current value.
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }
}


