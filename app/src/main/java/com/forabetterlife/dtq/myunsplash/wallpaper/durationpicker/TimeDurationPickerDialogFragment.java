package com.forabetterlife.dtq.myunsplash.wallpaper.durationpicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * Created by DTQ on 5/29/2018.
 */

public class TimeDurationPickerDialogFragment
        extends DialogFragment implements TimeDurationPickerDialog.OnDurationSetListener {
    private static final String TAG = "TimeDuration";
    public static final String EXTRA_DURATION =
            "com.forabetterlife.dtq.myunsplash.wallpaper.durationpicker.duration";

    public static final String BUNDLE_KEY_DURATION = "bundle_key_duration";

    public static TimeDurationPickerDialogFragment newInstance(long duration) {
        TimeDurationPickerDialogFragment dialogFragment = new TimeDurationPickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_KEY_DURATION, duration);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimeDurationPickerDialog(getActivity(), this, getInitialDuration(), setTimeUnits());
    }

    /**
     * The duration to be shown as default value when the dialog appears.
     * @return the default duration in milliseconds.
     */
    protected long getInitialDuration() {
        if (getArguments() != null) {
            return getArguments().getLong(BUNDLE_KEY_DURATION);
        } else {
            return 0;
        }
    }

    protected int setTimeUnits(){
        return TimeDurationPicker.HH_MM_SS;
    }

    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        Log.i(TAG, "duration is: " + String.valueOf(duration));
        sendResult(Activity.RESULT_OK, duration);
    }

    private void sendResult(int resultCode, long duration) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DURATION, duration);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}