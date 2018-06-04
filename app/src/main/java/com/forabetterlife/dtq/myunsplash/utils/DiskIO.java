package com.forabetterlife.dtq.myunsplash.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by DTQ on 3/25/2018.
 */

public class DiskIO implements Executor {
    private Executor mExecutor;

    public DiskIO() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mExecutor.execute(command);
    }
}
