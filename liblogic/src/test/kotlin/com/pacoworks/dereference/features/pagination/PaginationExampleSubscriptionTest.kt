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

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.core.functional.None
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class PaginationExampleSubscriptionTest {
    @Test
    fun emptyList_EndOfPage_LoadPage() {
        val initialList = listOf<String>()
        val newList = listOf("1", "2")
        val mockPaginationView = MockPaginationExampleOutputView()
        val pages = createStateHolder(0)
        val elements = createStateHolder(initialList)
        val state = PaginationExampleState(elements = elements, pages = pages)
        val service = { id: Int -> Observable.just(newList) }
        val pagesSubscriber = TestSubscriber<Int>()
        val elementsSubscriber = TestSubscriber<List<String>>()
        handleLoading(mockPaginationView, state, service)
        pages.subscribe(pagesSubscriber)
        elements.subscribe(elementsSubscriber)
        /* Assert initial state is empty */
        pagesSubscriber.assertValueCount(1)
        pagesSubscriber.assertValue(0)
        elementsSubscriber.assertValueCount(1)
        elementsSubscriber.assertValue(initialList)
        /* View reaches end of page */
        mockPaginationView.endOfPagePRelay.call(None.VOID)
        /* New elements loaded */
        pagesSubscriber.assertValueCount(2)
        pagesSubscriber.assertValues(0, 1)
        elementsSubscriber.assertValueCount(2)
        elementsSubscriber.assertValues(initialList, newList)
    }

    @Test
    fun filledList_EndOfPage_AppendEnd() {
        val initialList = listOf("1", "2")
        val newList = listOf("3", "4")
        val mockPaginationView = MockPaginationExampleOutputView()
        val pages = createStateHolder(0)
        val elements = createStateHolder(initialList)
        val state = PaginationExampleState(elements = elements, pages = pages)
        val service = { id: Int -> Observable.just(newList) }
        val pagesSubscriber = TestSubscriber<Int>()
        val elementsSubscriber = TestSubscriber<List<String>>()
        handleLoading(mockPaginationView, state, service)
        pages.subscribe(pagesSubscriber)
        elements.subscribe(elementsSubscriber)
        /* Assert initial state is empty */
        pagesSubscriber.assertValueCount(1)
        pagesSubscriber.assertValue(0)
        elementsSubscriber.assertValueCount(1)
        elementsSubscriber.assertValue(initialList)
        /* View reaches end of page */
        mockPaginationView.endOfPagePRelay.call(None.VOID)
        /* New elements loaded */
        pagesSubscriber.assertValueCount(2)
        pagesSubscriber.assertValues(0, 1)
        elementsSubscriber.assertValueCount(2)
        elementsSubscriber.assertValues(initialList, initialList.plus(newList))
    }
}

class MockPaginationExampleOutputView : PaginationExampleOutputView {
    val endOfPagePRelay: PublishRelay<None> = PublishRelay.create()

    override fun endOfPage(): Observable<None> = endOfPagePRelay
}
