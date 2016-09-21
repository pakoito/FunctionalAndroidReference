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

package com.pacoworks.dereference.features.global

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.ui.Direction
import com.pacoworks.dereference.core.ui.createHome
import com.pacoworks.dereference.core.ui.Screen
import org.javatuples.Pair

class AppState(
    val navigation: BehaviorRelay<Pair<Screen, Direction>> = BehaviorRelay.create(Pair.with(createHome(), Direction.FORWARD))
)