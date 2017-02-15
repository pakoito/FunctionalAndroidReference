/*
 * Copyright (c) pakoito 2017
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

import com.jakewharton.rxrelay.Relay
import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.architecture.ui.createStateHolder

object ReplUtils {
    fun <T: Any> c(value: T, name: String = "StateHolder"): StateHolder<T> =
            createStateHolder(value)
                    .apply {
                        zipWith(skip(1), { one, two -> one to two })
                                .subscribe { (one, two) -> println("$name updated!\nFROM:\n$one\nINTO:\n$two\n") }
                    }

    fun <T: Any, U: Relay<T, T>> u(state: U, value: T): Unit =
            state.call(value)

    infix fun <T: Any, U: Relay<T, T>> T.into(state: U): Unit =
            u(state, this)

    fun <T: Any, U: Relay<T, T>> p(state: U): Unit =
            println(state.toBlocking().first().toString())
}