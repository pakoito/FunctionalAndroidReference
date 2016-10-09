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

import com.jakewharton.rxrelay.PublishRelay
import com.pacoworks.dereference.architecture.navigation.Direction
import com.pacoworks.dereference.architecture.navigation.Screen
import com.pacoworks.dereference.architecture.navigation.createHome
import com.pacoworks.dereference.architecture.navigation.createRotation
import com.pacoworks.dereference.architecture.ui.createStateHolder
import org.javatuples.Pair
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class HomeInteractorSubscriptionTest {
    @Test
    fun userClick_UpdateScreen() {
        val view = MockHomeViewOutput()
        val startScreen = createHome()
        val startState = Pair.with(startScreen, Direction.FORWARD)
        val navigation = createStateHolder(startState)
        subscribeHomeInteractor(view, navigation)
        val testSubscriber = TestSubscriber.create<Pair<Screen, Direction>>()
        navigation.subscribe(testSubscriber)
        /* Starting value */
        testSubscriber.assertValueCount(1)
        /* Click on screen */
        val newScreen = createRotation()
        val newState = Pair.with(newScreen, Direction.FORWARD)
        view.screenClick.call(newScreen)
        /* Assert new value is seen */
        testSubscriber.assertValueCount(2)
        testSubscriber.assertValues(startState, newState)
    }

}

private class MockHomeViewOutput : HomeViewOutput {
    val screenClick: PublishRelay<Screen> = PublishRelay.create()

    override fun buttonClick(): Observable<Screen> =
            screenClick
}
