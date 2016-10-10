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

import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle
import org.junit.Test
import rx.observers.TestSubscriber

class ReactiveControllerTest {
    @Test
    fun conductorBuddy_OnLifecycleEvents_SeeAllEvents() {
        val reactiveController = ReactiveController()
        val reactiveBuddy = reactiveController.createBuddy()
        val testSubscriber = TestSubscriber.create<ControllerLifecycle>()
        reactiveBuddy.lifecycle().subscribe(testSubscriber)
        /* Call all lifecycle events */
        reactiveController.onEnter()
        reactiveController.onCreate()
        reactiveController.onAttach()
        reactiveController.onDetach()
        reactiveController.onDestroy()
        reactiveController.onFinish()
        /* Assert all lifecycle events seen */
        val values = ControllerLifecycle.values()
        testSubscriber.assertValueCount(values.count())
        testSubscriber.assertValues(*values)
        testSubscriber.assertNoTerminalEvent()
    }

}