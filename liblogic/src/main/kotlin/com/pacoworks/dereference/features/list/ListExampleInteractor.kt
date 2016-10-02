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

import com.pacoworks.rxcomprehensions.RxComprehensions
import rx.Subscription
import java.util.*

fun bindListExample(viewInput: ListExampleInputView, state: ListExampleState) {
    viewInput.createBinder<List<String>>().call(state.elements, viewInput::updateElements)
}

fun subscribeListExampleInteractor(viewOutput: ListExampleOutputView, state: ListExampleState): Subscription =
        RxComprehensions.doFM(
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