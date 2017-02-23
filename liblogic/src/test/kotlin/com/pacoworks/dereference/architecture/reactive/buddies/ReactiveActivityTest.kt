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
import org.junit.Test
import rx.observers.TestSubscriber

class ReactiveActivityTest {
    @Test
    fun activityBuddy_OnLifecycleEvents_SeeAllEvents() {
        val reactiveActivity = ReactiveActivity()
        val reactiveBuddy = reactiveActivity.createBuddy()
        val testSubscriber = TestSubscriber.create<ActivityLifecycle>()
        reactiveBuddy.lifecycle().subscribe(testSubscriber)
        /* Call all lifecycle events */
        reactiveActivity.onEnter()
        reactiveActivity.onCreate()
        reactiveActivity.onStart()
        reactiveActivity.onResume()
        reactiveActivity.onPause()
        reactiveActivity.onStop()
        reactiveActivity.onDestroy()
        reactiveActivity.onExit()
        /* Assert all lifecycle events seen */
        val values = ActivityLifecycle.values()
        testSubscriber.assertValueCount(values.count())
        testSubscriber.assertValues(*values)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun activityBuddy_activityResult_SeeAllEvents() {
        val reactiveActivity = ReactiveActivity()
        val reactiveBuddy = reactiveActivity.createBuddy()
        val testSubscriber = TestSubscriber.create<ActivityResult>()
        reactiveBuddy.activityResult().subscribe(testSubscriber)
        /* Call all possible activity results */
        val success = ActivityResult.Success(1)
        reactiveActivity.onActivityResult(success)
        val successWithData = ActivityResult.SuccessWithData(1, mapOf("a" to "b"))
        reactiveActivity.onActivityResult(successWithData)
        val failure = ActivityResult.Failure(2)
        reactiveActivity.onActivityResult(failure)
        val failureWithData = ActivityResult.FailureWithData(2, mapOf("b" to "a"))
        reactiveActivity.onActivityResult(failureWithData)
        /* Assert results received */
        testSubscriber.assertValueCount(4)
        testSubscriber.assertValues(success, successWithData, failure, failureWithData)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun activityBuddy_permissionResult_SeeAllEvents() {
        val reactiveActivity = ReactiveActivity()
        val reactiveBuddy = reactiveActivity.createBuddy()
        val testSubscriber = TestSubscriber.create<PermissionResult>()
        reactiveBuddy.permissionResult().subscribe(testSubscriber)
        /* Call all possible permission results */
        val success = PermissionResult.Success(1, "Hello")
        reactiveActivity.onPermissionResult(success)
        val failure = PermissionResult.Failure(2, "World")
        reactiveActivity.onPermissionResult(failure)
        /* Check results have been seen */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(success, failure)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun activityBuddy_backPressed_SeeAllEvents() {
        val reactiveActivity = ReactiveActivity()
        val reactiveBuddy = reactiveActivity.createBuddy()
        val testSubscriber = TestSubscriber.create<None>()
        reactiveBuddy.back().subscribe(testSubscriber)
        /* Press back */
        reactiveActivity.onBackPressed()
        /* Assert back seen */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(None)
        testSubscriber.assertNoTerminalEvent()
    }

}