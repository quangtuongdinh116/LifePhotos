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
import com.google.common.base.Strings;

public class SettingsPrefActivity extends AppCompatPreferenceActivity{
    private static final String TAG = "SettingsPrefActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");





        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment())
                .commit();
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



    public static class PrefFragment extends PreferenceFragment implements SettingsContract.View{

        private static SettingsContract.Presenter mPresenter;

        IntentFilter intentFilter;

        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if(intent.getIntExtra(WantedPhotoRemote.BROADCAST_INTENT_EXTRA_REQUEST_CODE,0) == 0) {
                        Snackbar.make(getView(),"You have new wanted photo!",Snackbar.LENGTH_LONG).show();
                        setResultCode(Activity.RESULT_CANCELED);
                    }
                }
            }
        };

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            intentFilter = new IntentFilter(WantedPhotoRemote.BROADCAST_ACTION);

            mPresenter = new SettingsPresenter(Inject.provideRepository(getActivity()),this);

            final ListPreference ListDownloadQualityPref = (ListPreference) findPreference("download_quality_list_key");
            final ListPreference ListLoadQualityPref = (ListPreference) findPreference("show_quality_list_key");
            final EditTextPreference photoSearchKeywordPref = (EditTextPreference) findPreference(getResources().getString(R.string.photo_wanted_edit_text_preference_key));
//            final SwitchPreference photoSearchSwitchPref = (SwitchPreference) findPreference(getResources().getString(R.string.photo_wanted_switch_preference_key));


            setListPreferenceData(ListDownloadQualityPref);
            setListPreferenceData(ListLoadQualityPref);



            addListenerToPreference(ListDownloadQualityPref,sPreferenceChangeListener);
            ListDownloadQualityPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    setListPreferenceData(ListDownloadQualityPref);
                    return false;
                }
            });

            addListenerToPreference(ListLoadQualityPref,sPreferenceChangeListener);
            ListDownloadQualityPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    setListPreferenceData(ListLoadQualityPref);
                    return false;
                }
            });

            addListenerToPreference(photoSearchKeywordPref,sPreferenceChangeListener);
//            addListenerToBooleanPreference(photoSearchSwitchPref,sPreferenceChangeListener);


        }

        @Override
        public void onStart() {
            super.onStart();
            getActivity().registerReceiver(mBroadcastReceiver,intentFilter,WantedPhotoRemote.PERMISSION,null);
        }

        @Override
        public void onStop() {
            super.onStop();
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }

        @Override
        public void setPresenter(SettingsContract.Presenter presenter) {

        }

        @Override
        public void showScheduleSuccessMessage() {
            if (getView() != null) {
                Snackbar.make(getView(),"Schedule success!",Snackbar.LENGTH_LONG).show();
            }

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
    }

    public static void addListenerToPreference(Preference preference, Preference.OnPreferenceChangeListener listener) {
        preference.setOnPreferenceChangeListener(listener);
        sPreferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static void addListenerToBooleanPreference(Preference preference, Preference.OnPreferenceChangeListener listener) {
        preference.setOnPreferenceChangeListener(listener);
//        sPreferenceChangeListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getBoolean(preference.getKey(), true));
    }



    private static Preference.OnPreferenceChangeListener sPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.i("HELLO","INSIDE onPreferenceChange");
            String value = newValue.toString();

                if (preference instanceof ListPreference) {
                    Log.i("HELLO","INSIDE preference instanceof ListPreference");
                    ListPreference listPreference = (ListPreference) preference;
//                    String chosenValue = listPreference.getValue();
//                    String chosenEntry = listPreference.getEntries()[Integer.parseInt(chosenValue)-1].toString();
//                    listPreference.setSummary(chosenEntry);
                    int index = listPreference.findIndexOfValue(value);
                    listPreference.setSummary(index >= 0? listPreference.getEntries()[index] : null);
                } else if (preference instanceof EditTextPreference) {
                    Log.i("HELLO","INSIDE preference instanceof EditTextPreference");
                    EditTextPreference editTextPreference = (EditTextPreference) preference;
                    editTextPreference.setSummary(value);
                } else if (preference instanceof SwitchPreference) {
                    Log.i(TAG, "inside value of switch change");
                    boolean isOn = Boolean.valueOf(value);
                    Log.i(TAG, "isOn is: " + String.valueOf(isOn));
                    PrefFragment.mPresenter.changeWantedPhotoServiceStatus(isOn, preference.getContext());
                }

            return true;
        }
    };

    protected static void setListPreferenceData(ListPreference lp) {
        CharSequence[] entries = { "REGULAR","SMALL","THUMB"};
        CharSequence[] entryValues = {"REGULAR","SMALL","THUMB"};
        lp.setEntries(entries);
        lp.setDefaultValue("REGULAR");
        lp.setEntryValues(entryValues);
    }


}
