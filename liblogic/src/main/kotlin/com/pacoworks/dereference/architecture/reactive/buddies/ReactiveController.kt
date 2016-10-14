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
import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle

/**
 * Delegate class for Android lifecycle responsibilities in a Controller screen to transform them on reactive streams.
 *
 * It wraps the lifecycle in a more comprehensible approach for this app.
 */
class ReactiveController {

    val lifecycleRelay: BehaviorRelay<ControllerLifecycle> = BehaviorRelay.create<ControllerLifecycle>()

    private fun call(lifecycle: ControllerLifecycle) = lifecycleRelay.call(lifecycle)

    /**
     * To be called when a Controller screen is created for the first time
     *
     * Note that Conductor doesn't handle retaining state after an app is destroyed by the OS
     */
    fun onEnter() = call(ControllerLifecycle.Enter)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onCreate() = call(ControllerLifecycle.Create)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onAttach() = call(ControllerLifecycle.Attach)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onDetach() = call(ControllerLifecycle.Detach)

    /**
     * To be called on the lifecycle method of the same name
     */
    fun onDestroy() = call(ControllerLifecycle.Destroy)

    /**
     * To be called when a Controller screen is finished by a business request
     */
    fun onExit() = call(ControllerLifecycle.Exit)

    fun createBuddy() = object : ControllerReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()
    }
}