package com.forabetterlife.dtq.myunsplash.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by DTQ on 3/25/2018.
 */

public class AppExecutors {

    @NonNull
    private Executor diskIO;

    @NonNull
    private Executor networkIO;

    @NonNull
    private Executor mainThread;

    public AppExecutors(@NonNull Executor diskIO, @NonNull Executor networkIO, @NonNull Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    @NonNull
    public Executor getDiskIO() {
        return diskIO;
    }

    @NonNull
    public Executor getNetworkIO() {
        return networkIO;
    }

    @NonNull
    public Executor getMainThread() {
        return mainThread;
    }

    public AppExecutors() {
        this(new DiskIO(), Executors.newFixedThreadPool(3),new MainThreadExecutor());
    }

    public static class MainThreadExecutor implements Executor {
        Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}
