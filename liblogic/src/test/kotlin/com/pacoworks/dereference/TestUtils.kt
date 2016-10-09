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

package com.pacoworks.dereference

import com.jakewharton.rxrelay.SerializedRelay
import rx.functions.Action2
import rx.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

fun <T> bindAsTest(): Action2<SerializedRelay<T, T>, (T) -> Unit> =
        Action2 { state, view -> state.observeOn(Schedulers.immediate()).subscribe(view) }

fun <T> mockView(callCount: AtomicLong = AtomicLong(0), value: AtomicReference<T> = AtomicReference()): (T) -> Unit =
        {
            value.set(it)
            callCount.andIncrement
        }