/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.dereference.features.global;

import android.app.Application;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.pacoworks.dereference.core.functional.Lazy;
import com.pacoworks.dereference.network.AgotApi;
import com.pacoworks.dereference.network.AgotApiKt;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.timber.data.LumberYard;
import kotlin.jvm.functions.Function0;
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

    private final Lazy<AgotApi> agotApiLazy = new Lazy<>(new Function0<AgotApi>() {
        @Override
        public AgotApi invoke() {
            return AgotApiKt.createAgotApi(okHttpClient);
        }
    });

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

    public AgotApi getAgotApi() {
        return agotApiLazy.get();
    }
}
