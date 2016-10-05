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

import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.SerializedRelay
import com.pacoworks.dereference.architecture.ui.StateHolder
import rx.Observable


data class PaginationExampleState(
        val elements: StateHolder<List<String>> = SerializedRelay(BehaviorRelay.create<List<String>>(Observable.range(0, 10).map { it.toString() }.toList().toBlocking().first())),
        val pages: StateHolder<Int> = SerializedRelay(BehaviorRelay.create(0))
)