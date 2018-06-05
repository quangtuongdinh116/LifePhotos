package com.forabetterlife.dtq.myunsplash.wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.forabetterlife.dtq.myunsplash.MyUnSplash;
import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.wallpaper.durationpicker.TimeDurationPickerDialogFragment;
import com.forabetterlife.dtq.myunsplash.wallpaper.durationpicker.TimeDurationUtil;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DTQ on 5/26/2018.
 */

public class WallpaperFragment extends Fragment implements WallpaperContract.View {

    private static final String DIALOG_DURATION = "DialogDuration";

    private static final int REQUEST_DURATION = 0;

    private Spinner mSpinner;
    private Button mStartStopButton;
    private Button mIntervalButton;
    private TextView mIntervalTV;
    private ArrayAdapter<String> mArrayAdapter;

    private WallpaperContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_auto_wallpaper, container, false);

        mSpinner = (Spinner) root.findViewById(R.id.spinner);
        mStartStopButton = (Button) root.findViewById(R.id.start_stop_button);
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    mPresenter.changeAutoWallpaperStatus(mPresenter.getDuration(),mSpinner.getSelectedItem().toString(),getContext());
                } else {
                    //do nothing
                }

            }
        });

        mIntervalButton = (Button) root.findViewById(R.id.interval_button);
        mIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                long duration = mPresenter.getDuration();

                    TimeDurationPickerDialogFragment dialogFragment = TimeDurationPickerDialogFragment.newInstance(duration);
                            dialogFragment.setTargetFragment(WallpaperFragment.this, REQUEST_DURATION);
                    dialogFragment.show(manager, DIALOG_DURATION);

            }
        });



        ArrayList<String> spinnerValues = new ArrayList<>();
        spinnerValues.add(MyUnSplash.FAVORITE);
        spinnerValues.add(MyUnSplash.WANTED_PHOTO);
        spinnerValues.add(MyUnSplash.RANDOM_PHOTO);

        mArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, spinnerValues);
        mSpinner.setAdapter(mArrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return root;
    }

    private boolean validate() {
        if (mPresenter.getDuration() < 30000) {
            Snackbar.make(getView(), getString(R.string.not_enough_interval),Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.wallpaper_title));
        mPresenter.takeView(this);
        mPresenter.loadStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DURATION) {
            long duration = (long) data
                    .getSerializableExtra(TimeDurationPickerDialogFragment.EXTRA_DURATION);
            showDuration(duration);
            mPresenter.setDuration(duration);

        }
    }

    private void showDuration(long duration) {
        int hour = TimeDurationUtil.hoursOf(duration);
        String showingHour = hour>0? hour + " Hours " : hour + " Hour ";
        int minute = TimeDurationUtil.minutesInHourOf(duration);
        String showingMinute = minute>0? minute + " Minutes " : hour + " Minute ";
        int second = TimeDurationUtil.secondsInMinuteOf(duration);
        String showingSecond = second>0? second + " Seconds " : hour + " Second ";
        mIntervalButton.setText(showingHour + showingMinute + showingSecond);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView(this);
    }

    @Override
    public void setPresenter(WallpaperContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showStatus(String type, boolean isOn, long duration) {
        mPresenter.setDuration(duration);
        showDuration(duration);
       for (int i = 0; i < mArrayAdapter.getCount(); i++) {
           if (type.trim().equals(mArrayAdapter.getItem(i).toString())) {
               mSpinner.setSelection(i);
           }
       }
       if(isOn) {
           mStartStopButton.setText(getString(R.string.stop_button));
       } else {
           mStartStopButton.setText(getString(R.string.start_button));
       }
    }

    @Override
    public void showScheduleSuccess() {
        if (getView() != null) {
            Snackbar.make(getView(), "Schedule success!",Snackbar.LENGTH_LONG).show();
            mStartStopButton.setText(getString(R.string.stop_button));
        }
    }

    @Override
    public void showScheduleFail() {
        if (getView() != null) {
            Snackbar.make(getView(), "Schedule fail!",Snackbar.LENGTH_LONG).show();
            mStartStopButton.setText(getString(R.string.start_button));
        }
    }

    @Override
    public void showStopSuccess() {
        if (getView() != null) {
            Snackbar.make(getView(), "Stop success!",Snackbar.LENGTH_LONG).show();
            mStartStopButton.setText(getString(R.string.start_button));
        }
    }

    @Override
    public void showStopFail() {
        if (getView() != null) {
            Snackbar.make(getView(), "Stop fail!",Snackbar.LENGTH_LONG).show();
            mStartStopButton.setText(getString(R.string.stop_button));
        }
    }
}
