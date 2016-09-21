@file:JvmName("HomeInteractor")

package com.pacoworks.dereference.features.home

import rx.subscriptions.CompositeSubscription

fun start(view: HomeView) {
    subscribeHomeInteractor(view)
    bindHomeInteractor(view)
}

private fun subscribeHomeInteractor(view: HomeView) =
        CompositeSubscription()

private fun bindHomeInteractor(view: HomeView) =
    view.createBinder<String>().call(title, { view.setTitle(it) })