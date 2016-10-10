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
import com.pacoworks.dereference.features.rotation.services.TransactionRequest
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import java.util.concurrent.TimeUnit

class RotationSubscribeLoadTest {
    @Test
    fun idle_loadRequest_TransitionToResponse() {
        val initialState = Transaction.Idle
        val serverResponse = Transaction.Success(BookCharacter("Paco"))
        val transactionHolder: StateHolder<Transaction> = createStateHolder(initialState)
        val services: TransactionRequest = { value -> Observable.just(serverResponse) }
        val testSubscriber = TestSubscriber<Transaction>()
        handleLoad(transactionHolder, services)
        transactionHolder.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Transition to loading */
        val loadingState = Transaction.Loading(UserInput("Hello"))
        transactionHolder.call(loadingState)
        /* Check state has moved correctly */
        testSubscriber.assertValueCount(3)
        testSubscriber.assertValues(initialState, loadingState, serverResponse)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun loading_newRequest_ReceiveOnlyLatest() {
        val initialState = Transaction.Idle
        val transactionHolder: StateHolder<Transaction> = createStateHolder(initialState)
        /* Delay to allow switchMap to happen on the new state */
        val services: TransactionRequest = { value -> Observable.just<Transaction>(Transaction.Success(BookCharacter(value))).delay(500, TimeUnit.MILLISECONDS) }
        val testSubscriber = TestSubscriber<Transaction>()
        handleLoad(transactionHolder, services)
        transactionHolder.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Transition to loading */
        val oldInput = "Hello"
        val loadingState = Transaction.Loading(UserInput(oldInput))
        transactionHolder.call(loadingState)
        /* A new loading request comes */
        val latestInput = "Paco"
        val otherLoadingState = Transaction.Loading(UserInput(latestInput))
        transactionHolder.call(otherLoadingState)
        /* Await for next state */
        transactionHolder.skip(1).toBlocking().first()
        /* Check state has moved to latest */
        testSubscriber.assertValueCount(4)
        /* Expected response for next state is latestInput, not oldInput */
        val expectedResponse = Transaction.Success(BookCharacter(latestInput))
        testSubscriber.assertValues(initialState, loadingState, otherLoadingState, expectedResponse)
        testSubscriber.assertNoTerminalEvent()
    }

}