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
import com.pacoworks.dereference.mockView
import com.pacoworks.rxsealedunions.Union1
import com.pacoworks.rxsealedunions.generic.GenericUnions
import org.javatuples.Pair
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class NavigationPushTest {
    @Test
    fun homeScreen_navigateToNewScreen_NewScreenPushed() {
        val homeScreen = createHome()
        val initialScreen = Pair.with(homeScreen, Direction.FORWARD)
        val navigation = createStateHolder(initialScreen)
        val navigator = MockForwardNavigatorView()
        pushScreen(MockActivityReactiveBuddy(), navigator, navigation, Schedulers.immediate())
        /* Initial value skipped because it'll be the already displayed screen after binding  */
        assertEquals(0L, navigator.goToCount.get())
        /* Push new screen */
        val newScreen = createRotation()
        val newScreenTransaction = Pair.with(newScreen, Direction.FORWARD)
        navigation.call(newScreenTransaction)
        /* See that screen has been received */
        assertEquals(1L, navigator.goToCount.get())
        assertEquals(newScreen, navigator.goToValue.get())
    }
}

private class MockForwardNavigatorView : NavigatorView {
    private val BACK_RESULT_FACTORY: Union1.Factory<Screen> = GenericUnions.singletFactory()

    override fun goBack(): Union1<Screen> = BACK_RESULT_FACTORY.none()

    val goToCount = AtomicLong()

    val goToValue = AtomicReference<Screen>()

    override fun goTo(screen: Screen) =
            mockView(goToCount, goToValue).invoke(screen)
}

