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
import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.UserInput
import org.junit.Test
import rx.observers.TestSubscriber

class RotationSubscribeStartTest {
    @Test
    fun idle_validINput_transitionToLoading() {
        val userInputPRelay = PublishRelay.create<UserInput>()
        val initialTransactionState = Transaction.Idle
        val transaction: StateHolder<Transaction> = createStateHolder(initialTransactionState)
        val testSubscriber = TestSubscriber.create<Transaction>()
        transaction.subscribe(testSubscriber)
        handleStart(userInputPRelay, transaction)
        /* Initial state is idle until input */
        testSubscriber.assertValueCount(1)
        /* As soon as a valid input comes */
        val input = "Hello"
        val userInput = UserInput(input)
        userInputPRelay.call(userInput)
        /* The state transitions to Loading */
        testSubscriber.assertValueCount(2)
        val newTransactionState = Transaction.Loading(userInput)
        testSubscriber.assertValues(initialTransactionState, newTransactionState)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun idle_invalidInput_InputIgnored() {
        val userInputPRelay = PublishRelay.create<UserInput>()
        val initialTransactionState = Transaction.Idle
        val transaction: StateHolder<Transaction> = createStateHolder(initialTransactionState)
        val testSubscriber = TestSubscriber.create<Transaction>()
        transaction.subscribe(testSubscriber)
        handleStart(userInputPRelay, transaction)
        /* Initial state is idle until input */
        testSubscriber.assertValueCount(1)
        /* Invalid input is ignored */
        val invalidInput = UserInput("")
        userInputPRelay.call(invalidInput)
        testSubscriber.assertValueCount(1)
        testSubscriber.assertNoTerminalEvent()
    }
}