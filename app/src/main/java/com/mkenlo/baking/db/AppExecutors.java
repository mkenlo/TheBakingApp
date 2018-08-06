package com.mkenlo.baking.db;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    private static AppExecutors ourInstance;

    private static final Object LOCK = new Object();

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }


    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public static AppExecutors getInstance(){
        synchronized (LOCK){
            if (ourInstance == null) {
                ourInstance =   new AppExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
            return ourInstance;
        }

    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
