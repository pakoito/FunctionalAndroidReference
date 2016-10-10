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

class ReactiveController {

    val lifecycleRelay: BehaviorRelay<ControllerLifecycle> = BehaviorRelay.create<ControllerLifecycle>()

    private fun call(lifecycle: ControllerLifecycle) = lifecycleRelay.call(lifecycle)

    fun onEnter() = call(ControllerLifecycle.Enter)

    fun onCreate() = call(ControllerLifecycle.Create)

    fun onAttach() = call(ControllerLifecycle.Attach)

    fun onDetach() = call(ControllerLifecycle.Detach)

    fun onDestroy() = call(ControllerLifecycle.Destroy)

    fun onFinish() = call(ControllerLifecycle.Exit)

    fun createBuddy() = object : ControllerReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()
    }
}