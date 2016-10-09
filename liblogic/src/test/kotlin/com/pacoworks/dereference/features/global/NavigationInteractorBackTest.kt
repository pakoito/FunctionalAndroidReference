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

import com.pacoworks.dereference.architecture.navigation.*
import com.pacoworks.dereference.architecture.ui.createStateHolder
import com.pacoworks.dereference.core.functional.None
import com.pacoworks.dereference.mockView
import com.pacoworks.rxsealedunions.Union1
import com.pacoworks.rxsealedunions.generic.GenericUnions
import org.javatuples.Pair
import org.junit.Test
import rx.observers.TestSubscriber

class NavigationInteractorBackTest {
    @Test
    fun navigationAtFirstScreen_backPressed_HomeScreen() {
        val activityReactiveBuddy = MockActivityReactiveBuddy()
        val back = activityReactiveBuddy.back
        val initialScreen = Pair.with(createRotation(), Direction.FORWARD)
        val state = AppState(navigation = createStateHolder(initialScreen))
        /* Our navigator doesn't have a screen to return to */
        val navigator = object : MockBackNavigatorView() {
            override fun goBack(): Union1<Screen> = BACK_RESULT_FACTORY.none()
        }
        backPressed(activityReactiveBuddy, navigator, state)
        val testSubscriber = TestSubscriber<Pair<Screen, Direction>>()
        state.navigation.subscribe(testSubscriber)
        /* See initial screen */
        testSubscriber.assertValueCount(1)
        /* Back pressed */
        back.call(None.VOID)
        /* Assert that we return to HomeScreen */
        testSubscriber.assertValueCount(2)
        val startScreen = Pair.with(createHome(), Direction.FORWARD)
        testSubscriber.assertValues(initialScreen, startScreen)
    }

    @Test
    fun navigationAtManyScreens_backPressed_PreviousScreen() {
        val activityReactiveBuddy = MockActivityReactiveBuddy()
        val back = activityReactiveBuddy.back
        val initialScreen = Pair.with(createRotation(), Direction.FORWARD)
        val state = AppState(navigation = createStateHolder(initialScreen))
        val previousScreen = createDragAndDrop()
        /* Our navigator returns to an existing screen */
        val navigator = object : MockBackNavigatorView() {
            override fun goBack(): Union1<Screen> {
                return BACK_RESULT_FACTORY.first(previousScreen)
            }
        }
        backPressed(activityReactiveBuddy, navigator, state)
        val testSubscriber = TestSubscriber<Pair<Screen, Direction>>()
        state.navigation.subscribe(testSubscriber)
        /* See initial screen */
        testSubscriber.assertValueCount(1)
        /* Back pressed */
        back.call(None.VOID)
        testSubscriber.assertValueCount(2)
        /* Assert that we return to previous screen */
        val startScreen = Pair.with(previousScreen, Direction.BACK)
        testSubscriber.assertValues(initialScreen, startScreen)
    }
}

private abstract class MockBackNavigatorView : NavigatorView {
    override fun goTo(screen: Screen) =
            mockView<Screen>().invoke(screen)
}

private val BACK_RESULT_FACTORY: Union1.Factory<Screen> = GenericUnions.singletFactory()


