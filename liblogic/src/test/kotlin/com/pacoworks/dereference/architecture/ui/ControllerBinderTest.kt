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
import com.pacoworks.dereference.architecture.reactive.ConductorLifecycle
import com.pacoworks.dereference.mockView
import org.junit.Assert
import org.junit.Test
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class ControllerBinderTest {

    @Test
    fun bindEnter_valueReceived_ViewNotBound() {
        val lifecycleObservable = BehaviorRelay.create<ConductorLifecycle>(ConductorLifecycle.Enter)
        val mainThreadScheduler = Schedulers.immediate()
        val state = createStateHolder(1)
        val callCount = AtomicLong()
        val doView = mockView<Int>(callCount)
        bind(lifecycleObservable, mainThreadScheduler, state, doView)
        Assert.assertEquals(0L, callCount.get())
        /* State changes not forwarded */
        state.call(3)
        Assert.assertEquals(0L, callCount.get())
    }

    @Test
    fun bindEnter_LifecycleAttach_ViewBound() {
        val lifecycleObservable = BehaviorRelay.create<ConductorLifecycle>(ConductorLifecycle.Enter)
        val mainThreadScheduler = Schedulers.immediate()
        val state = createStateHolder(1)
        val callCount = AtomicLong()
        val doView = mockView<Int>(callCount)
        bind(lifecycleObservable, mainThreadScheduler, state, doView)
        Assert.assertEquals(0L, callCount.get())
        lifecycleObservable.call(ConductorLifecycle.Attach)
        /* Initial state is applied */
        Assert.assertEquals(1L, callCount.get())
        /* State changes applied now */
        state.call(2)
        Assert.assertEquals(2L, callCount.get())
    }

    @Test
    fun bindAttach_LifecycleDetach_ViewUnbound() {
        val lifecycleObservable = BehaviorRelay.create<ConductorLifecycle>(ConductorLifecycle.Attach)
        val mainThreadScheduler = Schedulers.immediate()
        val state = createStateHolder(1)
        val callCount = AtomicLong()
        val doView = mockView<Int>(callCount)
        bind(lifecycleObservable, mainThreadScheduler, state, doView)
        Assert.assertEquals(1L, callCount.get())
        state.call(2)
        Assert.assertEquals(2L, callCount.get())
        lifecycleObservable.call(ConductorLifecycle.Detach)
        /* State changes after detach are not applied */
        state.call(3)
        Assert.assertEquals(2L, callCount.get())
    }

    @Test
    fun bindAttach_ValueReceived_ReceivedOnScheduler() {
        val lifecycleObservable = BehaviorRelay.create<ConductorLifecycle>(ConductorLifecycle.Attach)
        val mainThreadScheduler = Schedulers.io()
        val state = createStateHolder(1)
        val latch = CountDownLatch(1)
        val threadName = AtomicReference<String>("")
        val doView = { string: Int ->
            threadName.set(Thread.currentThread().name)
            latch.countDown()
        }
        bind(lifecycleObservable, mainThreadScheduler, state, doView)
        latch.await()
        Assert.assertTrue(threadName.get().contains("RxIoScheduler"))
    }
}