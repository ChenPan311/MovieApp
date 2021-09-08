package com.example.movieapp.Utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskIO;
    private final Executor networkIo;
    private final Executor mainThread;

    public AppExecutors(Executor diskIO, Executor networkIo, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIo = networkIo;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3),
                    new MainThreadExecutor());
        }
        return instance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIo() {
        return networkIo;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
