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

import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.rotation.model.UserInput
import org.junit.Test
import rx.observers.TestSubscriber
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RotationSubscribeUserInputTest {
    @Test
    fun emptyInput_validInput_NewValue() {
        val initialValue = UserInput("")
        val userState = createStateHolder(initialValue)
        val testSubscriber = TestSubscriber<UserInput>()
        val view = MockRotationViewOutput()
        handleUserInput(view, userState)
        userState.subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        val validInput = "35"
        view.userInputPRelay.call(validInput)
        /* Block until new state is debounced */
        userState.skip(1).toBlocking().first()
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(initialValue, UserInput(validInput))
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun validInput_newInput_NewValue() {
        val initialValue = UserInput("Hello")
        val userState = createStateHolder(initialValue)
        val testSubscriber = TestSubscriber<UserInput>()
        val view = MockRotationViewOutput()
        handleUserInput(view, userState)
        userState.subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        val validInput = "72"
        view.userInputPRelay.call(validInput)
        /* Block until new state is debounced */
        userState.skip(1).toBlocking().first()
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(initialValue, UserInput(validInput))
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun validInput_invalidInput_OldValue() {
        val initialValue = UserInput("Hello")
        val userState = createStateHolder(initialValue)
        val testSubscriber = TestSubscriber<UserInput>()
        val view = MockRotationViewOutput()
        handleUserInput(view, userState)
        /* Timeout on no new values */
        userState.timeout(3, TimeUnit.SECONDS).subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        val invalidInput = "Error"
        view.userInputPRelay.call(invalidInput)
        /* Wait for result not to come */
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertValueCount(1)
        /* Assert that no new values were received even after debounce */
        testSubscriber.assertError(TimeoutException::class.java)
    }

    @Test
    fun validInput_sameInput_OldValue() {
        val initialInput = "Hello"
        val initialValue = UserInput(initialInput)
        val userState = createStateHolder(initialValue)
        val testSubscriber = TestSubscriber<UserInput>()
        val view = MockRotationViewOutput()
        handleUserInput(view, userState)
        /* Timeout on no new values */
        userState.timeout(3, TimeUnit.SECONDS).subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        view.userInputPRelay.call(initialInput)
        /* Wait for result not to come */
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertValueCount(1)
        /* Assert that no new values were received even after debounce */
        testSubscriber.assertError(TimeoutException::class.java)
    }

}