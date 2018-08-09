package com.forabetterlife.dtq.myunsplash.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Modal bottom sheet. This is a version of {@link DialogFragment} that shows a bottom sheet
 * using {@link android.support.design.widget.BottomSheetDialog} instead of a floating dialog.
 */
public class BottomSheetDialogFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }

}
