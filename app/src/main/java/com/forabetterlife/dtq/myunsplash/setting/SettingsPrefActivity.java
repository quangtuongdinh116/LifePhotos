package com.forabetterlife.dtq.myunsplash.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.remote.wantedphoto.WantedPhotoRemote;
import com.forabetterlife.dtq.myunsplash.prod.Inject;
import com.forabetterlife.dtq.myunsplash.utils.ThemeUtils;
import com.google.common.base.Strings;

public class SettingsPrefActivity extends AppCompatPreferenceActivity{
    private static final String TAG = "SettingsPrefActivity";

    public static final String THEME_CHANGE_KEY = "theme_change_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        switch (ThemeUtils.getTheme(this)) {
            case ThemeUtils.Theme.DARK_GREEN:
                setTheme(R.style.AppTheme);
                break;
            case ThemeUtils.Theme.BLACK:
                setTheme(R.style.AppTheme_Black_ActionBar);
                break;
        }

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static class PrefFragment extends PreferenceFragment implements SettingsContract.View, Preference.OnPreferenceChangeListener {


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            final ListPreference ListDownloadQualityPref = (ListPreference) findPreference("download_quality_list_key");
            final ListPreference ListLoadQualityPref = (ListPreference) findPreference("show_quality_list_key");
            final ListPreference ListThemePref = (ListPreference) findPreference("theme_key");
            final EditTextPreference photoSearchKeywordPref = (EditTextPreference) findPreference(getResources().getString(R.string.photo_wanted_edit_text_preference_key));

            ListDownloadQualityPref.setOnPreferenceChangeListener(this);
            ListLoadQualityPref.setOnPreferenceChangeListener(this);
            ListThemePref.setOnPreferenceChangeListener(this);
            photoSearchKeywordPref.setOnPreferenceChangeListener(this);


            ListThemePref.setSummary(ListThemePref.getEntry());
            ListDownloadQualityPref.setSummary(ListDownloadQualityPref.getEntry());
            ListLoadQualityPref.setSummary(ListLoadQualityPref.getEntry());

            String searhKeyword = PreferenceManager.getDefaultSharedPreferences(photoSearchKeywordPref.getContext())
                    .getString(getResources().getString(R.string.photo_wanted_edit_text_preference_key),"");
            photoSearchKeywordPref.setText(searhKeyword);
            photoSearchKeywordPref.setSummary(searhKeyword);
        }


        @Override
        public void setPresenter(SettingsContract.Presenter presenter) {

        }

        @Override
        public void showScheduleSuccessMessage() {
            showSnackBar(getString(R.string.schedule_success_message));
        }

        @Override
        public void showScheduleFailMessage() {
            if (getView() != null) {
                Snackbar.make(getView(),"Schedule fail!",Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void showStopSuccessMessage() {
            if (getView() != null) {
                Snackbar.make(getView(),"Stop success!",Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void showStopFailMessage() {
            if (getView() != null) {
                Snackbar.make(getView(),"Stop fail!",Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void showSnackBar(String message) {
            if (getView() == null)
                return;
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }

        private void restartActivity(){
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;

                    listPreference.setValue(o.toString());

                    preference.setSummary(listPreference.getEntry());

                    if (preference.getKey().equals("theme_key")) {
                        getActivity().setResult(Activity.RESULT_OK);
                        restartActivity();
                    }
                } else if (preference instanceof EditTextPreference) {
                    String value = o.toString();
                    EditTextPreference editTextPreference = (EditTextPreference) preference;
                    editTextPreference.setSummary(value);
                }
            return true;
        }
        }
    }





