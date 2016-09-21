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

package com.pacoworks.dereference.core.ui

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.reactive.ConductorLifecycle
import rx.Subscription

fun <T> bind(lifecycleObservable: rx.Observable<ConductorLifecycle>, mainThreadScheduler: rx.Scheduler, state: BehaviorRelay<T>, doView: (T) -> Unit): Subscription =
        lifecycleObservable
                .filter { it == ConductorLifecycle.Attach }
                .takeUntil { it == ConductorLifecycle.Detach }
                .flatMap { state }
                .observeOn(mainThreadScheduler)
                .subscribe(doView)