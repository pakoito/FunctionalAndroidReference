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
import com.pacoworks.dereference.architecture.navigation.createRotation
import com.pacoworks.dereference.mockView
import org.javatuples.Pair
import org.junit.Assert
import org.junit.Test
import rx.Observable
import rx.functions.Action1
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class HomeInteractorSubscriptionTest {
    @Test
    fun userClick_UpdateScreen() {
        val callCount = AtomicLong()
        val value = AtomicReference<Pair<Screen, Direction>>()
        val mockNavigation = mockView(callCount, value)
        val view = MockHomeViewOutput()
        subscribeHomeInteractor(view, Action1 { mockNavigation.invoke(it) })
        /* No value selected */
        Assert.assertEquals(0L, callCount.get())
        /* Click on screen */
        val newScreen = createRotation()
        view.screenClick.call(newScreen)
        /* Assert new value is seen */
        Assert.assertEquals(1L, callCount.get())
        Assert.assertEquals(newScreen, value.get().value0)
    }

}

private class MockHomeViewOutput : HomeViewOutput {
    val screenClick: PublishRelay<Screen> = PublishRelay.create()

    override fun buttonClick(): Observable<Screen> =
            screenClick
}
