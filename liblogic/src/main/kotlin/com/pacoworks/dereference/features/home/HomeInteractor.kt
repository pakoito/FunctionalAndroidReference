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

package com.pacoworks.dereference.features.home

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.features.global.Direction
import com.pacoworks.dereference.features.global.Screen
import org.javatuples.Pair
import rx.Subscription

fun subscribeHomeInteractor(view: HomeViewOutput, navigation: Lazy<StateHolder<Pair<Screen, Direction>>>): Subscription =
        view.buttonClick()
                .map { Pair.with(it, Direction.FORWARD) }
                /* Using method reference doesn't typecheck. Call it explicitly instead */
                .subscribe { navigation.value.call(it) }
