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

import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class ListSubscribeCommitDeleteTest {
    @Test
    fun selectedElements_CommitDeletion_ElementsRemoved() {
        val initialId = "12"
        val editMode = createStateHolder(createEditModeDelete(initialId))
        val initialList = Observable.range(0, 50).map { it.toString() }.toList().toBlocking().first().toList()
        val elementsState = createStateHolder(initialList)
        val selected = createStateHolder(setOf(initialId))
        val testSubscriber = TestSubscriber.create<List<String>>()
        handleOnCommitDelete(editMode, elementsState, selected)
        elementsState.subscribe(testSubscriber)
        /* Initial state */
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(initialList)
        /* Select some elements */
        val newSelected = setOf("12", "15", "1", "20")
        selected.call(newSelected)
        /* Transition to normal mode */
        editMode.call(createEditModeNormal())
        /* Assert elements have been deleted */
        testSubscriber.assertValueCount(2)
        val expectedList = initialList.filter { !newSelected.contains(it) }
        testSubscriber.assertValues(initialList, expectedList)
        testSubscriber.assertNoTerminalEvent()

    }

}