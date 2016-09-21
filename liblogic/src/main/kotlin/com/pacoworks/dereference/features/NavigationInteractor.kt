package com.pacoworks.dereference.features

import com.pacoworks.dereference.reactive.ActivityLifecycle
import com.pacoworks.dereference.reactive.buddies.ActivityReactiveBuddy
import rx.Subscription
import rx.subscriptions.CompositeSubscription

fun subscribeNavigation(state: AppState, navigator: Navigator, activityReactiveBuddy: ActivityReactiveBuddy): Subscription =
        CompositeSubscription(backPressed(activityReactiveBuddy, navigator), pushScreen(activityReactiveBuddy, navigator, state))

private fun backPressed(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator): Subscription =
        activityReactiveBuddy.back()
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Exit })
                .subscribe { navigator.goBack() }

private fun pushScreen(activityReactiveBuddy: ActivityReactiveBuddy, navigator: Navigator, state: AppState) =
        state.navigation
                .takeUntil(activityReactiveBuddy.lifecycle().filter { it == ActivityLifecycle.Exit })
                .subscribe { navigator.goTo(it) }