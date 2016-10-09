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

package com.pacoworks.dereference.features.global

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.reactive.ActivityLifecycle
import com.pacoworks.dereference.architecture.reactive.ActivityResult
import com.pacoworks.dereference.architecture.reactive.PermissionResult
import com.pacoworks.dereference.architecture.reactive.buddies.ActivityReactiveBuddy
import com.pacoworks.dereference.core.functional.None
import rx.Observable

class MockActivityReactiveBuddy : ActivityReactiveBuddy {
    val lifecyclePRelay: PublishRelay<ActivityLifecycle> = PublishRelay.create()

    val activityResult: PublishRelay<ActivityResult> = PublishRelay.create()

    val permissionResult: PublishRelay<PermissionResult> = PublishRelay.create()

    val back: PublishRelay<None> = PublishRelay.create()

    override fun lifecycle(): Observable<ActivityLifecycle> =
            lifecyclePRelay

    override fun activityResult(): Observable<ActivityResult> =
            activityResult

    override fun permissionResult(): Observable<PermissionResult> =
            permissionResult

    override fun back(): Observable<None> =
            back

}