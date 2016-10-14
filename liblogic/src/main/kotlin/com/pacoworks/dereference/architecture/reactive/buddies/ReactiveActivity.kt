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

package com.pacoworks.dereference.architecture.reactive.buddies

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.architecture.reactive.ActivityLifecycle
import com.pacoworks.dereference.architecture.reactive.ActivityResult
import com.pacoworks.dereference.architecture.reactive.PermissionResult
import com.pacoworks.dereference.core.functional.None

/**
 * Delegate class for Android lifecycle responsibilities in an Activity to transform them on reactive streams.
 *
 * It wraps the lifecycle in a more comprehensible approach for this app.
 */
class ReactiveActivity {

    val lifecycleRelay: BehaviorRelay<ActivityLifecycle> = BehaviorRelay.create<ActivityLifecycle>()

    val activityResultRelay: BehaviorRelay<ActivityResult> = BehaviorRelay.create<ActivityResult>()

    val permissionResultRelay: BehaviorRelay<PermissionResult> = BehaviorRelay.create<PermissionResult>()

    val onBackRelay: BehaviorRelay<None> = BehaviorRelay.create<None>()

    private fun call(lifecycle: ActivityLifecycle) = lifecycleRelay.call(lifecycle)

    /**
     * To be called on the first time an Activity is created
     */
    fun onEnter() = call(ActivityLifecycle.Enter)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onCreate() = call(ActivityLifecycle.Create)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onStart() = call(ActivityLifecycle.Start)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onResume() = call(ActivityLifecycle.Resume)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onPause() = call(ActivityLifecycle.Pause)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onStop() = call(ActivityLifecycle.Stop)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onDestroy() = call(ActivityLifecycle.Destroy)

    /**
     * To be called when an Activity is finished by a business request
     */
    fun onExit() = call(ActivityLifecycle.Exit)

    /**
     * To be called after receiving a result from another Activity
     */
    fun onActivityResult(result: ActivityResult) = activityResultRelay.call(result)

    /**
     * To be called after receiving a result of a permission request
     */
    fun onPermissionResult(result: PermissionResult) = permissionResultRelay.call(result)

    /**
     * To be called when the user presses the back key
     */
    fun onBackPressed() = onBackRelay.call(None.VOID)

    /**
     * Creates a proxy object [ActivityReactiveBuddy] to access framework events, like lifecycle.
     *
     * @return a new [ActivityReactiveBuddy]
     */
    fun createBuddy() = object : ActivityReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()

        override fun activityResult() = activityResultRelay.asObservable()

        override fun permissionResult() = permissionResultRelay.asObservable()

        override fun back() = onBackRelay.asObservable()
    }
}