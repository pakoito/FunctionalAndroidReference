package com.pacoworks.dereference;

import android.app.Application;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.pacoworks.dereference.features.AppState;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.timber.data.LumberYard;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class Injector {
    private static final long CACHE_SIZE = 10L * 1024L * 1024L; /* 10 MiB */

    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s

    private static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s

    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

    private final AppState state = new AppState();

    private final OkHttpClient okHttpClient;

    public Injector(Application application) {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .cache(new Cache(application.getCacheDir(), CACHE_SIZE))
                .build();

        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(application);
        LumberYard lumberYard = LumberYard.getInstance(application);
        lumberYard.cleanUp();

        Timber.plant(lumberYard.tree());
        Timber.plant(new Timber.DebugTree());

        TinyDancer.create()
                .show(application);
    }

    public AppState getState() {
        return state;
    }

    public OkHttpClient getHttpClient() {
        return okHttpClient;
    }
}
