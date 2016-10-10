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

package com.pacoworks.dereference.features.list

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.core.functional.None
import com.pacoworks.dereference.features.list.model.Delete
import com.pacoworks.dereference.features.list.model.Normal
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import com.pacoworks.rxsealedunions.Union2
import org.junit.Test
import rx.observers.TestSubscriber

class ListSubscribeExitEditTest {
    @Test
    fun editState_DeleteClick_ExitEdit() {
        val initialState = createEditModeDelete("1")
        val editMode = createStateHolder(initialState)
        val deleteClicks = PublishRelay.create<None>()
        handleExitEditState(deleteClicks, editMode)
        val testSubscriber = TestSubscriber<Union2<Normal, Delete>>()
        editMode.subscribe(testSubscriber)
        /* Initial value */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Select position */
        deleteClicks.call(None.VOID)
        /* Assert transition to edit complete*/
        testSubscriber.assertValueCount(2)
        val endState = createEditModeNormal()
        testSubscriber.assertValues(initialState, endState)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun normalState_DeleteClick_NoChanges() {
        val initialState = createEditModeNormal()
        val editMode = createStateHolder(initialState)
        val deleteClicks = PublishRelay.create<None>()
        handleExitEditState(deleteClicks, editMode)
        val testSubscriber = TestSubscriber<Union2<Normal, Delete>>()
        editMode.subscribe(testSubscriber)
        /* Initial value */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Select position */
        deleteClicks.call(None.VOID)
        /* Assert no transition was done because the state is already delete */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        testSubscriber.assertNoTerminalEvent()
    }

}