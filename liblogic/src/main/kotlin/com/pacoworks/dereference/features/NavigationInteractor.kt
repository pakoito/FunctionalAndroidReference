package com.pacoworks.dereference.features

import com.pacoworks.dereference.core.ui.Navigator
import com.pacoworks.dereference.core.reactive.ActivityLifecycle
import com.pacoworks.dereference.core.reactive.buddies.ActivityReactiveBuddy
import rx.Scheduler
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun subscribeNavigation(state: AppState, navigator: Navigator, activityReactiveBuddy: ActivityReactiveBuddy, mainThreadScheduler: Scheduler): Subscription =
        CompositeSubscription(
                pushScreen(activityReactiveBuddy, navigator, state, mainThreadScheduler),
                backPressed(activityReactiveBuddy, navigator, state))

private fun pushScreen(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator, state: AppState, mainThreadScheduler: Scheduler) =
        state.navigation
                /* Skip the first value to avoid re-pushing the current value after rotation */
                .skip(1)
                .filter { it.value1 == Direction.FORWARD }
                .map { it.value0 }
                .observeOn(mainThreadScheduler)
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .subscribe { navigator.goTo(it) }

private fun backPressed(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator, state: AppState): Subscription =
        activityReactiveBuddy.back()
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Destroy })
                .map {
                    navigator.goBack()
                            .join(
                                    // Traverse the stack
                                    { org.javatuples.Pair.with(it, Direction.BACK) },
                                    // Restart app
                                    { org.javatuples.Pair.with(createHome(), Direction.FORWARD) }
                            )
                }.subscribe(state.navigation)
