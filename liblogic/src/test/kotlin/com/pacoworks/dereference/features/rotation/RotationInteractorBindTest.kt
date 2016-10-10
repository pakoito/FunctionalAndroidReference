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

import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.bindAsTest
import com.pacoworks.dereference.features.rotation.model.BookCharacter
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.UserInput
import com.pacoworks.dereference.mockView
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.functions.Action2
import java.util.concurrent.atomic.AtomicLong

class RotationInteractorBindTest {
    @Test
    fun idle_StateLoading_CallLoading() {
        val transaction = createStateHolder<Transaction>(Transaction.Idle)
        val state = RotationState(transaction = transaction)
        val view = MockRotationViewInput()
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        bindRotationInteractor(view, state)
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        transaction.call(Transaction.Loading(UserInput("1")))
        assertEquals(1, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
    }

    @Test
    fun idle_StateError_CallError() {
        val transaction = createStateHolder<Transaction>(Transaction.Idle)
        val state = RotationState(transaction = transaction)
        val view = MockRotationViewInput()
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        bindRotationInteractor(view, state)
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        transaction.call(Transaction.Failure("Something went wrong"))
        assertEquals(0, view.loadingCount.get())
        assertEquals(1, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
    }

    @Test
    fun idle_StateWaiting_CallWaiting() {
        val transaction = createStateHolder<Transaction>(Transaction.Idle)
        val state = RotationState(transaction = transaction)
        val view = MockRotationViewInput()
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        bindRotationInteractor(view, state)
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        transaction.call(Transaction.WaitingForRetry(10))
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(1, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
    }

    @Test
    fun idle_StateSuccess_Success() {
        val transaction = createStateHolder<Transaction>(Transaction.Idle)
        val state = RotationState(transaction = transaction)
        val view = MockRotationViewInput()
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        bindRotationInteractor(view, state)
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(0, view.successCount.get())
        transaction.call(Transaction.Success(BookCharacter("1")))
        assertEquals(0, view.loadingCount.get())
        assertEquals(0, view.errorCount.get())
        assertEquals(0, view.waitingCount.get())
        assertEquals(1, view.successCount.get())
    }
}

class MockRotationViewInput : RotationViewInput {
    val loadingCount = AtomicLong()
    val errorCount = AtomicLong()
    val waitingCount = AtomicLong()
    val successCount = AtomicLong()

    override fun setLoading(user: String) =
            mockView<String>(loadingCount).invoke(user)

    override fun showError(reason: String) =
            mockView<String>(errorCount).invoke(reason)

    override fun setWaiting(seconds: Int) =
            mockView<Int>(waitingCount).invoke(seconds)

    override fun showRepos(value: String) =
            mockView<String>(successCount).invoke(value)

    override fun <T> createBinder(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
            bindAsTest()
}