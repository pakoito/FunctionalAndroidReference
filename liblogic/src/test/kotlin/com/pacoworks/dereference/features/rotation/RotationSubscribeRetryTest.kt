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

package com.pacoworks.dereference.features.rotation

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle
import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.UserInput
import org.junit.Test
import rx.observers.TestSubscriber

class RotationSubscribeRetryTest {
    @Test
    fun failure_WaitUntilReload_LoadNewValue() {
        val initialValue = "Hello"
        val initialInput = UserInput(initialValue)
        val userState = createStateHolder(initialInput)
        val lifecycle = PublishRelay.create<ControllerLifecycle>()
        val initialState = Transaction.Idle
        val transactionState: StateHolder<Transaction> = createStateHolder(initialState)
        val testSubscriber = TestSubscriber<Transaction>()
        transactionState.subscribe(testSubscriber)
        handleRetryAfterError(userState, transactionState, lifecycle)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Kickstart transaction */
        val loadTransaction = Transaction.Failure("")
        transactionState.call(loadTransaction)
        /* Block until next Loading state */
        val endState = Transaction.Loading(initialInput)
        transactionState.filter { it == endState }.toBlocking().first()
        /* Assert all values are seen */
        testSubscriber.assertValueCount(9)
        testSubscriber.assertValues(initialState, loadTransaction,
                countDown(5), countDown(4), countDown(3), countDown(2), countDown(1), countDown(0),
                endState)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun waitingReload_LifecycleExit_NoReload() {
        val initialValue = "Hello"
        val initialInput = UserInput(initialValue)
        val userState = createStateHolder(initialInput)
        val lifecycle = PublishRelay.create<ControllerLifecycle>()
        val initialState = Transaction.Idle
        val transactionState: StateHolder<Transaction> = createStateHolder(initialState)
        val testSubscriber = TestSubscriber<Transaction>()
        transactionState.subscribe(testSubscriber)
        handleRetryAfterError(userState, transactionState, lifecycle)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Cancel after two states have been received */
        /* Kickstart transaction */
        val loadTransaction = Transaction.Failure("")
        transactionState.call(loadTransaction)
        /* Finish the transaction when 4 is seen */
        transactionState.filter { it == countDown(4) }.doOnNext { lifecycle.call(ControllerLifecycle.Exit) }.toBlocking().first()
        /* Assert all values until disconnection are seen */
        testSubscriber.assertValueCount(4)
        testSubscriber.assertValues(initialState, loadTransaction,
                countDown(5), countDown(4))
        testSubscriber.assertNoTerminalEvent()
    }

    private fun countDown(value: Int) =
            Transaction.WaitingForRetry(value)

}