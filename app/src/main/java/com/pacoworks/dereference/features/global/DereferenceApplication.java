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
import android.content.Context;

/**
 * Application class. It holds the global injection module.
 */
public class DereferenceApplication extends Application {
    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        injector = new Injector(this);
    }

    /**
     * Access the {@link DereferenceApplication} single instance.
     * 
     * @param context
     * @return
     */
    public static DereferenceApplication get(Context context) {
        return (DereferenceApplication)context.getApplicationContext();
    }

    /**
     * Retrieves the global injection module
     * 
     * @return the module
     */
    public Injector getInjector() {
        return injector;
    }
}
