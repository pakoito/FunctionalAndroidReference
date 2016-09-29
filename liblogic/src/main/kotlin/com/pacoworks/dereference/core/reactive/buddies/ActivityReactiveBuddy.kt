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

import com.pacoworks.dereference.core.functional.None
import com.pacoworks.dereference.core.reactive.ActivityLifecycle
import com.pacoworks.dereference.core.reactive.ActivityResult
import com.pacoworks.dereference.core.reactive.PermissionResult
import rx.Observable

interface ActivityReactiveBuddy {
    fun lifecycle(): Observable<ActivityLifecycle>

    fun activityResult(): Observable<ActivityResult>

    fun permissionResult(): Observable<PermissionResult>

    fun back(): Observable<None>
}