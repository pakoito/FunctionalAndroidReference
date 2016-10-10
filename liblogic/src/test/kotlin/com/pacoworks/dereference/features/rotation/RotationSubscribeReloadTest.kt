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

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.rotation.model.BookCharacter
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.UserInput
import org.junit.Test
import rx.observers.TestSubscriber

class RotationSubscribeReloadTest {

    @Test
    fun success_newInput_LoadNewValue() {
        val initialValue = "Hello"
        val initialInput = UserInput(initialValue)
        val userInputPRelay: StateHolder<UserInput> = createStateHolder(initialInput)
        val initialState = Transaction.Idle
        val transaction: StateHolder<Transaction> = createStateHolder(initialState)
        val testSubscriber = TestSubscriber<Transaction>()
        transaction.subscribe(testSubscriber)
        handleReload(userInputPRelay, transaction)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Transition to success */
        val successState = Transaction.Success(BookCharacter(initialValue))
        transaction.call(successState)
        /* New input */
        val newValue = "World"
        val newInput = UserInput(newValue)
        userInputPRelay.call(newInput)
        /* Assert new loading state */
        testSubscriber.assertValueCount(3)
        val expectedState = Transaction.Loading(newInput)
        testSubscriber.assertValues(initialState, successState, expectedState)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun success_sameInput_NoTransition() {
        val initialValue = "Hello"
        val initialInput = UserInput(initialValue)
        val userInputPRelay: StateHolder<UserInput> = createStateHolder(initialInput)
        val initialState = Transaction.Idle
        val transaction: StateHolder<Transaction> = createStateHolder(initialState)
        val testSubscriber = TestSubscriber<Transaction>()
        transaction.subscribe(testSubscriber)
        handleReload(userInputPRelay, transaction)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Transition to success */
        val successState = Transaction.Success(BookCharacter(initialValue))
        transaction.call(successState)
        /* New input */
        userInputPRelay.call(initialInput)
        /* Assert new loading state */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(initialState, successState)
        testSubscriber.assertNoTerminalEvent()
    }

}