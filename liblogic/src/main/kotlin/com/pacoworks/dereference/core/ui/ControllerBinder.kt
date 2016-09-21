package com.pacoworks.dereference.core.ui

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.reactive.ConductorLifecycle
import rx.Observable
import rx.Scheduler
import rx.Subscription

fun <T> bind(lifecycleObservable: rx.Observable<com.pacoworks.dereference.core.reactive.ConductorLifecycle>, mainThreadScheduler: rx.Scheduler, state: com.jakewharton.rxrelay.BehaviorRelay<T>, doView: (T) -> Unit): rx.Subscription =
        lifecycleObservable
                .filter { it == com.pacoworks.dereference.core.reactive.ConductorLifecycle.Attach }
                .takeUntil { it == com.pacoworks.dereference.core.reactive.ConductorLifecycle.Detach }
                .flatMap { state }
                .observeOn(mainThreadScheduler)
                .subscribe(doView)