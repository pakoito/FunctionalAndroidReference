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
import com.pacoworks.dereference.architecture.navigation.Direction
import com.pacoworks.dereference.architecture.navigation.Navigator
import com.pacoworks.dereference.architecture.navigation.Screen
import com.pacoworks.dereference.architecture.navigation.createHome
import com.pacoworks.dereference.architecture.reactive.ActivityLifecycle
import com.pacoworks.dereference.architecture.reactive.buddies.ActivityReactiveBuddy
import org.javatuples.Pair
import rx.Scheduler
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun subscribeNavigation(state: AppState, navigator: Navigator, activityReactiveBuddy: ActivityReactiveBuddy, mainThreadScheduler: Scheduler): Subscription =
        CompositeSubscription(
                pushScreen(activityReactiveBuddy, navigator, state.navigation, mainThreadScheduler),
                backPressed(activityReactiveBuddy, navigator, state))

private fun pushScreen(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator, navigation: BehaviorRelay<Pair<Screen, Direction>>, mainThreadScheduler: Scheduler) =
        navigation
                /* Skip the first value to avoid re-pushing the current value after rotation */
                .skip(1)
                .filter { it.value1 == Direction.FORWARD }
                .map { it.value0 }
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .observeOn(mainThreadScheduler)
                .subscribe { navigator.goTo(it) }

private fun backPressed(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator, state: AppState): Subscription =
        activityReactiveBuddy.back()
                .map {
                    navigator.goBack()
                            .join(
                                    /* If back to screen, just forwards it */
                                    { screen -> Pair.with(screen, Direction.BACK) },
                                    /* If back to exit app, reset to initial state */
                                    { -> Pair.with(createHome(), Direction.FORWARD) }
                            )
                }
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .subscribe(state.navigation)
