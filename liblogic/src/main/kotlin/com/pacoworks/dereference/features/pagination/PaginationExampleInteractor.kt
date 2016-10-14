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

package com.pacoworks.dereference.features.pagination

import com.pacoworks.dereference.features.pagination.services.PaginationExampleService
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Binds the state of this use case to a [com.pacoworks.dereference.architecture.ui.BoundView]
 */
fun bindPaginationExample(viewInput: PaginationExampleInputView, state: PaginationExampleState) {
    viewInput.createBinder<List<String>>().call(state.elements, viewInput::updateElements)
}

/**
 * Subscribes all use cases in the file
 */
fun subscribePaginationExample(viewOutput: PaginationExampleOutputView, state: PaginationExampleState, service: PaginationExampleService): Subscription =
        CompositeSubscription(
                handleLoading(viewOutput, state, service)
        )

fun handleLoading(viewOutput: PaginationExampleOutputView, state: PaginationExampleState, service: PaginationExampleService): Subscription =
        doSM(
                { state.elements },
                { viewOutput.endOfPage().first() },
                { elements, click -> state.pages.first() },
                { elements, click, page ->
                    service.invoke(page)
                            .map { elements.plus(it) }
                            /* Update pages inline after a success */
                            .doOnNext { state.pages.call(page + 1) }
                }
        ).subscribe(state.elements)