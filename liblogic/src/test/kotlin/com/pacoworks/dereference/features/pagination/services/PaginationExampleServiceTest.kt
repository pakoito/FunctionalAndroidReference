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

package com.pacoworks.dereference.features.pagination.services

import org.junit.Assert.assertEquals
import org.junit.Test

class PaginationExampleServiceTest {
    @Test
    fun requestPage_getTenMoreResults() {
        val result = requestMore(4).toBlocking().first()
        val expectation = listOf("50", "51", "52", "53", "54", "55", "56", "57", "58", "59")
        assertEquals(expectation, result)
        val result2 = requestMore(0).toBlocking().first()
        val expectation2 = listOf("10", "11", "12", "13", "14", "15", "16", "17", "18", "19")
        assertEquals(expectation2, result2)
    }

}