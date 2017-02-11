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

package com.pacoworks.dereference.features.draganddrop

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.features.list.ListExampleState
import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import org.javatuples.Pair
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Binds the state of this use case to a [com.pacoworks.dereference.architecture.ui.BoundView]
 *
 * @see [com.pacoworks.dereference.architecture.ui.bind]
 */
fun bindDragAndDropExample(viewInput: DragAndDropInputView, state: ListExampleState) {
    viewInput.createBinder<List<String>>().call(state.elements, { viewInput.updateElements(it) })
    viewInput.createBinder<Set<String>>().call(state.selected, { viewInput.updateSelected(it) })
}

/**
 * Subscribes all use cases in the file
 */
fun subscribeDragAndDropInteractor(viewOutput: DragAndDropOutputView, state: ListExampleState): Subscription =
        CompositeSubscription(
                handleSelected(viewOutput.listClicks(), state.selected),
                handleDragAndDrop(viewOutput.dragAndDropMoves(), state.elements))

fun handleSelected(listClicks: Observable<Pair<Int, String>>, selected: StateHolder<Set<String>>): Subscription =
        doSM(
                /* For every value of selected */
                { selected },
                /* Wait for a user click on the list */
                { listClicks.map { it.value1 }.first() },
                { selected, item ->
                    /* Add or remove the clicked element from the selected list */
                    Observable.just(
                            if (selected.contains(item)) {
                                selected.minus(item)
                            } else {
                                selected.plus(item)
                            }
                    )
                }
        ).subscribe(selected)


fun handleDragAndDrop(dragAndDropObservable: Observable<Pair<Int, Int>>, elements: StateHolder<List<String>>): Subscription =
        doFM(
                /* For ever drag and drop operation */
                { dragAndDropObservable },
                { swap ->
                    /* Get the latest elements */
                    elements
                            .first()
                            /* Swap the elements from the drag and drop operation */
                            .map {
                                it.toMutableList()
                                        .apply { Collections.swap(this, swap.value0, swap.value1) }
                                        .toList()
                            }
                }
        ).subscribe(elements)