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

import com.pacoworks.dereference.architecture.reactive.ActivityLifecycle
import com.pacoworks.dereference.architecture.reactive.buddies.ActivityReactiveBuddy
import com.pacoworks.dereference.architecture.ui.StateHolder
import org.javatuples.Pair
import rx.Scheduler
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun subscribeNavigation(state: AppState, navigatorView: NavigatorView, activityReactiveBuddy: ActivityReactiveBuddy, mainThreadScheduler: Scheduler): Subscription =
        CompositeSubscription(
                pushScreen(activityReactiveBuddy, navigatorView, state.navigation, mainThreadScheduler),
                backPressed(activityReactiveBuddy, navigatorView, state))

fun pushScreen(activityReactiveBuddy: ActivityReactiveBuddy, navigatorView: NavigatorView, navigation: StateHolder<Pair<Screen, Direction>>, mainThreadScheduler: Scheduler): Subscription =
        navigation
                /* Skip the first value to avoid re-pushing the current value after rotation */
                .skip(1)
                .filter { it.value1 == Direction.FORWARD }
                .map { it.value0 }
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .observeOn(mainThreadScheduler)
                .subscribe { navigatorView.goTo(it) }

fun backPressed(activityReactiveBuddy: ActivityReactiveBuddy, navigatorView: NavigatorView, state: AppState): Subscription =
        activityReactiveBuddy.back()
                .map {
                    navigatorView.goBack()
                            .join(
                                    /* If back to screen, just forwards it */
                                    { screen -> Pair.with(screen, Direction.BACK) },
                                    /* If back to exit app, reset to initial state */
                                    { -> Pair.with(createHome(), Direction.FORWARD) }
                            )
                }
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .subscribe(state.navigation)
