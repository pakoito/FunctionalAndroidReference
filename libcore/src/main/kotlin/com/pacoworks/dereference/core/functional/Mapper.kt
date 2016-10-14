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

@file:JvmName("Mapper")

package com.pacoworks.dereference.core.functional

import rx.functions.Func1

/**
 * Maps a function to a value passed as a parameter
 *
 * @param value the parameter to return
 */
fun <T> just(value: T): Func1<in Any, T> {
    return Func1 { value }
}

/**
 * Maps a value before passing it to a function
 *
 * @param mapper mapping function
 * @param action function to be called
 */
fun <T, U> fmap(mapper: (T) -> U, action: (U) -> Unit): (T) -> Unit =
        { action.invoke(mapper.invoke(it)) }