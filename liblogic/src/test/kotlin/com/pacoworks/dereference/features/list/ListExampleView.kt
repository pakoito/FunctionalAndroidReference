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

import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.bindAsTest
import com.pacoworks.dereference.features.list.model.Delete
import com.pacoworks.dereference.features.list.model.Normal
import com.pacoworks.dereference.mockView
import com.pacoworks.rxsealedunions.Union2
import rx.functions.Action2
import java.util.concurrent.atomic.AtomicLong

class MockListInputExampleView : ListExampleInputView {
    val selectedCount = AtomicLong()

    val editModeCount = AtomicLong()

    val elementsCount = AtomicLong()

    override fun updateSelected(selected: Set<String>) =
            mockView<Set<String>>(selectedCount).invoke(selected)

    override fun updateEditMode(editMode: Union2<Normal, Delete>) =
            mockView<Union2<Normal, Delete>>(editModeCount).invoke(editMode)

    override fun updateElements(elements: List<String>) =
            mockView<List<String>>(elementsCount).invoke(elements)

    override fun <T: Any> createBinder(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
            bindAsTest()

}