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

import com.pacoworks.dereference.architecture.reactive.ActivityLifecycle
import com.pacoworks.dereference.architecture.reactive.ActivityResult
import com.pacoworks.dereference.architecture.reactive.PermissionResult
import com.pacoworks.dereference.core.functional.None
import rx.Observable

/**
 * Proxy interface to access Android framework responsibilities of a [ReactiveActivity]
 */
interface ActivityReactiveBuddy {
    /**
     * Non-terminating [Observable] representing the Activity lifecycle
     */
    fun lifecycle(): Observable<ActivityLifecycle>

    /**
     * Non-terminating [Observable] representing all results received for an Activity request
     */
    fun activityResult(): Observable<ActivityResult>

    /**
     * Non-terminating [Observable] representing all results received for a permission request
     */
    fun permissionResult(): Observable<PermissionResult>

    /**
     * Non-terminating [Observable] representing user back presses
     */
    fun back(): Observable<None>
}