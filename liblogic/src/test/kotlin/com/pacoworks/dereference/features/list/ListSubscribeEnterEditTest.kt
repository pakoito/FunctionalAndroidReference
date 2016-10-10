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
import com.pacoworks.dereference.features.list.model.Delete
import com.pacoworks.dereference.features.list.model.Normal
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import com.pacoworks.rxsealedunions.Union2
import org.javatuples.Pair
import org.junit.Test
import rx.observers.TestSubscriber

class ListSubscribeEnterEditTest {
    @Test
    fun normalState_LongPress_EnterEdit() {
        val initialState = createEditModeNormal()
        val editMode = createStateHolder(initialState)
        val listLongClicks = PublishRelay.create<Pair<Int, String>>()
        handleEnterEditState(listLongClicks, editMode)
        val testSubscriber = TestSubscriber<Union2<Normal, Delete>>()
        editMode.subscribe(testSubscriber)
        /* Initial value */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Select position */
        val selectedId = "1"
        listLongClicks.call(Pair.with(1, selectedId))
        /* Assert transition to edit complete*/
        testSubscriber.assertValueCount(2)
        val endState = createEditModeDelete(selectedId)
        testSubscriber.assertValues(initialState, endState)
        testSubscriber.assertNoTerminalEvent()
    }

    @Test
    fun editState_LongPress_NoChanges() {
        val initialState = createEditModeDelete("15")
        val editMode = createStateHolder(initialState)
        val listLongClicks = PublishRelay.create<Pair<Int, String>>()
        handleEnterEditState(listLongClicks, editMode)
        val testSubscriber = TestSubscriber<Union2<Normal, Delete>>()
        editMode.subscribe(testSubscriber)
        /* Initial value */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        /* Select position */
        val selectedId = "1"
        listLongClicks.call(Pair.with(1, selectedId))
        /* Assert no transition was done because the state is already delete */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialState)
        testSubscriber.assertNoTerminalEvent()
    }

}