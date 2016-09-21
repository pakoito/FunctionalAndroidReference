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

package com.pacoworks.dereference.core.reactive.buddies

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.reactive.ActivityLifecycle
import com.pacoworks.dereference.core.reactive.ActivityResult
import com.pacoworks.dereference.core.reactive.None
import com.pacoworks.dereference.core.reactive.PermissionResult

class ReactiveActivity {

    val lifecycleRelay = BehaviorRelay.create<ActivityLifecycle>()

    val activityResultRelay = BehaviorRelay.create<ActivityResult>()

    val permissionResultRelay = BehaviorRelay.create<PermissionResult>()

    val onBackRelay = BehaviorRelay.create<None>()

    private fun call(lifecycle: ActivityLifecycle) = lifecycleRelay.call(lifecycle)

    fun onEnter() = call(ActivityLifecycle.Enter)

    fun onCreate() = call(ActivityLifecycle.Create)

    fun onStart() = call(ActivityLifecycle.Start)

    fun onResume() = call(ActivityLifecycle.Resume)

    fun onPause() = call(ActivityLifecycle.Pause)

    fun onStop() = call(ActivityLifecycle.Stop)

    fun onDestroy() = call(ActivityLifecycle.Destroy)

    fun onFinish() = call(ActivityLifecycle.Exit)

    fun onActivityResult(result: ActivityResult) = activityResultRelay.call(result)

    fun onBackPressed() = onBackRelay.call(None.VOID)

    fun createBuddy() = object : ActivityReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()

        override fun activityResult() = activityResultRelay.asObservable()

        override fun permissionResult() = permissionResultRelay.asObservable()

        override fun back() = onBackRelay.asObservable()
    }
}