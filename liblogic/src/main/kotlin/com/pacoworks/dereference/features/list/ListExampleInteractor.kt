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
import com.pacoworks.dereference.core.functional.None
import com.pacoworks.dereference.features.list.model.EditMode
import com.pacoworks.dereference.features.list.model.createEditModeDelete
import com.pacoworks.dereference.features.list.model.createEditModeNormal
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import com.pacoworks.rxtuples.RxTuples
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
fun bindListExample(viewInput: ListExampleInputView, state: ListExampleState) {
    viewInput.createBinder<List<String>>().call(state.elements, viewInput::updateElements)
    viewInput.createBinder<Set<String>>().call(state.selected, viewInput::updateSelected)
    viewInput.createBinder<EditMode>().call(state.editMode, viewInput::updateEditMode)
}

/**
 * Subscribes all use cases in the file
 */
fun subscribeListExampleInteractor(viewOutput: ListExampleOutputView, state: ListExampleState): Subscription =
        CompositeSubscription(
                handleAdd(state.elements, viewOutput.addClick()),
                handleEnterEditState(viewOutput.listLongClicks(), state.editMode),
                handleExitEditState(viewOutput.deleteClick(), state.editMode),
                handleOnCommitDelete(state.editMode, state.elements, state.selected),
                /* FIXME: Must be after handleOnCommitDelete to assure that selections are not cleared before they're deleted */
                handleOnSwitchEditState(state.editMode, state.selected),
                handleSelect(state.editMode, state.selected, viewOutput.listClicks()))

fun handleAdd(elementsState: StateHolder<List<String>>, addClick: Observable<None>): Subscription =
        doSM(
                /* For the current list of elements */
                { elementsState },
                /* When the user clicks add */
                { addClick.first() },
                { elements, click ->
                    /* Insert random number at end of the list */
                    Observable.just(elements.plus((Math.abs(Random().nextInt()) + elements.size).toString()))
                }
        )
                .subscribe(elementsState)

fun handleEnterEditState(listLongClicks: Observable<Pair<Int, String>>, editMode: StateHolder<EditMode>): Subscription =
        /* When the user clicks on the list with a long press */
        listLongClicks
                .flatMap { click ->
                    /* And the mode is not edit */
                    editMode.first()
                            .filter { it.join({ normal -> true }, { delete -> false }) }
                            /* Push edit mode with the position pressed */
                            .map { createEditModeDelete(click.value1) }
                }.subscribe(editMode)

fun handleExitEditState(deleteClick: Observable<None>, editMode: StateHolder<EditMode>): Subscription =
        /* When the user clicks onthe delete button*/
        deleteClick
                .flatMap {
                    /* And the mode is edit ing */
                    editMode.first()
                            .filter { it.join({ normal -> false }, { delete -> true }) }
                            /* Push exiting edit mode */
                            .map { createEditModeNormal() }
                }.subscribe(editMode)

fun handleOnSwitchEditState(editMode: StateHolder<EditMode>, selected: StateHolder<Set<String>>): Subscription =
        /* When edit mode changes */
        editMode
                .map { it.join(
                        /* If exiting delete mode, remove elements from selected */
                        { normal -> setOf<String>() },
                        /* If entering delete mode, add the long pressed value element to selected */
                        { delete -> setOf(delete.id) }) }
                .subscribe(selected)

fun handleOnCommitDelete(editMode: StateHolder<EditMode>, elementsState: StateHolder<List<String>>, selected: StateHolder<Set<String>>): Subscription =
        doSM(
                /* When the mode changes from delete to normal */
                { editMode.filter { it.join({ normal -> true }, { delete -> false }) } },
                /* Get all elements, and the set of selected ones */
                { Observable.zip(elementsState, selected, RxTuples.toPair<List<String>, Set<String>>()).first() },
                /* Delete selected elements */
                { exitEditMode, statePair ->
                    Observable.from(statePair.value0).filter { !statePair.value1.contains(it) }.toList()
                }
        )
                .subscribe(elementsState)

fun handleSelect(editMode: StateHolder<EditMode>, selected: StateHolder<Set<String>>, listClicks: Observable<Pair<Int, String>>): Subscription =
        /* When the edit mode is delete */
        editMode.
                switchMap {
                    if (it.join({ normal -> false }, { delete -> true })) {
                        /* Pass through the delete clicks*/
                        listClicks.map { it.value1 }
                    } else {
                        /* Else ignore them */
                        Observable.empty()
                    }
                }
                .flatMap { value ->
                    /* Add or remove the clicked from the selected list */
                    selected.first().map {
                        if (it.contains(value)) {
                            it.minus(value)
                        } else {
                            it.plus(value)
                        }
                    }
                }.subscribe(selected)