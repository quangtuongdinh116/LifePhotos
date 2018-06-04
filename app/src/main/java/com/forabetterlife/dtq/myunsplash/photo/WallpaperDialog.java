package com.forabetterlife.dtq.myunsplash.photo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.forabetterlife.dtq.myunsplash.R;

/**
 * Created by DTQ on 3/23/2018.
 */

public class WallpaperDialog extends DialogFragment {

    public interface WallpaperDialogListener {
        void onCancel();
    }

    private WallpaperDialogListener listener;
    private boolean downloadFinished = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wallpaper, null, false);


        return new AlertDialog.Builder(getActivity())
                .setTitle("Setting wallpaper...")
                .setNegativeButton("CANCEL", null)
                .setView(view)
                .create();
    }

    public void setListener(WallpaperDialogListener listener) {
        this.listener = listener;
    }

    public void setDownloadFinished(boolean downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (!downloadFinished && listener != null) {
            listener.onCancel();
        }
        downloadFinished = false;
    }

}

