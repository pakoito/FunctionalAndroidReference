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

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import org.javatuples.Pair
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

fun bindListExample(viewInput: ListExampleInputView, state: ListExampleState) {
    viewInput.createBinder<List<String>>().call(state.elements, viewInput::updateElements)
    viewInput.createBinder<Set<String>>().call(state.selected, viewInput::updateSelected)
}

fun subscribeListExampleInteractor(viewOutput: ListExampleOutputView, state: ListExampleState): Subscription =
        CompositeSubscription(
                handleDragAndDrop(state, viewOutput),
                handleSelect(state.selected, viewOutput.listClicks()))

fun handleSelect(selected: StateHolder<Set<String>>, listClicks: Observable<Pair<Int, String>>): Subscription =
        doFM(
                { listClicks.map { it.value1 } },
                { value ->
                    selected.first().map {
                        if (it.contains(value)) {
                            it.minus(value)
                        } else {
                            it.plus(value)
                        }
                    }
                }
        ).subscribe(selected)

private fun handleDragAndDrop(state: ListExampleState, viewOutput: ListExampleOutputView): Subscription =
        doFM(
        {
            viewOutput.dragAndDropMoves()
        },
        { swap ->
            state.elements
                    .first()
                    .map {
                        it.toMutableList()
                                .apply { Collections.swap(this, swap.value0, swap.value1) }
                                .toList()
                    }
        }
).subscribe(state.elements)