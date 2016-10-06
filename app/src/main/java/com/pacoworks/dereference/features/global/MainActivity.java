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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.architecture.navigation.Navigator;
import com.pacoworks.dereference.architecture.reactive.ActivityResult;
import com.pacoworks.dereference.architecture.reactive.PermissionResult;
import com.pacoworks.dereference.architecture.reactive.buddies.ReactiveActivity;
import com.pacoworks.dereference.features.home.HomeScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.okhttp3.OkHttp3Module;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private final ReactiveActivity reactiveActivity = new ReactiveActivity();

    private DebugDrawer debugDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Injector injector = DereferenceApplication.get(this).getInjector();
        debugDrawer = new DebugDrawer.Builder(this)
                .modules(
                        new OkHttp3Module(injector.getHttpClient()),
                        new DeviceModule(this),
                        new BuildModule(this)
                ).build();
        ViewGroup container = (ViewGroup) findViewById(R.id.activity_main);
        Router router = Conductor.attachRouter(this, container, savedInstanceState);
        if (savedInstanceState == null) {
            reactiveActivity.onEnter();
            /* Push initial state manually */
            router.pushController(RouterTransaction.with(new HomeScreen()));
        }
        reactiveActivity.onCreate();
        Navigator navigator = new MainNavigator(router, this);
        MainOrchestrator.start(injector.getState(), navigator, reactiveActivity.createBuddy());
    }

    @Override
    protected void onStart() {
        super.onStart();
        debugDrawer.onStart();
        reactiveActivity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugDrawer.onResume();
        reactiveActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        debugDrawer.onPause();
        reactiveActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        debugDrawer.onStop();
        reactiveActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reactiveActivity.onDestroy();
        if (isFinishing()) {
            reactiveActivity.onFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (null != data && null != data.getExtras()) {
                final Bundle extras = data.getExtras();
                reactiveActivity.onActivityResult(new ActivityResult.SuccessWithData(requestCode, bundleToMap(extras)));
            } else {
                reactiveActivity.onActivityResult(new ActivityResult.Success(requestCode));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != data && null != data.getExtras()) {
                final Bundle extras = data.getExtras();
                reactiveActivity.onActivityResult(new ActivityResult.FailureWithData(requestCode, bundleToMap(extras)));
            } else {
                reactiveActivity.onActivityResult(new ActivityResult.Failure(requestCode));
            }
        }
    }

    private Map<String, Object> bundleToMap(Bundle extras) {
        HashMap<String, Object> results = new HashMap<>(extras.size());
        final Set<String> keys = extras.keySet();
        for (String key : keys) {
            results.put(key, extras.get(key));
        }
        return results;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PERMISSION_GRANTED) {
                reactiveActivity.getPermissionResultRelay().call(new PermissionResult.Success(requestCode, permissions[i]));
            } else if (grantResults[i] == PERMISSION_DENIED) {
                reactiveActivity.getPermissionResultRelay().call(new PermissionResult.Failure(requestCode, permissions[i]));
            }
        }
    }

    @Override
    public void onBackPressed() {
        reactiveActivity.onBackPressed();
    }
}
