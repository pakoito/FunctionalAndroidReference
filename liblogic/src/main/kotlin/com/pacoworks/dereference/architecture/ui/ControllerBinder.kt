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

package com.pacoworks.dereference.architecture.ui

import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle
import rx.Observable
import rx.Scheduler
import rx.Subscription

/**
 * A [SerializedRelay] backed by a [BehaviorRelay] used to represent the value of one piece of state over time
 */
typealias StateHolder<T> = SerializedRelay<T, T>

/**
 * Creates a new [StateHolder]
 */
fun <T> createStateHolder(value: T): StateHolder<T> =
        SerializedRelay(BehaviorRelay.create<T>(value))

/**
 * Binds a view to a [StateHolder] respecting the [com.pacoworks.dereference.features.global.BaseController] lifecycle, and the Android requirement to only modify views on the main thread
 */
fun <T> bind(lifecycleObservable: Observable<ControllerLifecycle>, mainThreadScheduler: Scheduler, state: StateHolder<T>, doView: (T) -> Unit): Subscription =
        lifecycleObservable
                .filter { it == ControllerLifecycle.Attach }
                /* Stop the previous state emissions and switch to the new one */
                .switchMap { state }
                .observeOn(mainThreadScheduler)
                /* Must be after observeOn to assure no races to main thread. See https://www.reddit.com/r/androiddev/comments/574suy/android_leak_pattern_subscriptions_in_views/d8v9l0w */
                .takeUntil(lifecycleObservable.filter { it == ControllerLifecycle.Detach })
                .subscribe(doView)