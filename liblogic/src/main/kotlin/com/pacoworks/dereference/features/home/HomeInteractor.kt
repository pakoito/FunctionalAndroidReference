package com.pacoworks.dereference.features.home

import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import rx.Subscription

fun start(view: HomeView, state: HomeState) {
    subscribeHomeInteractor(view, state)
    bindHomeInteractor(view, state)
}

private fun bindHomeInteractor(view: HomeView, state: HomeState) {
    view.createBinder<Int>().call(state.counter, { view.setTitle(it.toString()) })
}

private fun subscribeHomeInteractor(view: HomeView, state: HomeState) =
        startClicks(view, state)

private fun startClicks(view: HomeView, state: HomeState): Subscription =
        doFM(
                { view.clicks() },
                { state.counter.first().map { it + 1 } }
        ).subscribe(state.counter)
