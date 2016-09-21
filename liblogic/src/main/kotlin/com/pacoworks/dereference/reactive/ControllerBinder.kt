package com.pacoworks.dereference.reactive

import com.jakewharton.rxrelay.BehaviorRelay
import rx.Observable
import rx.Scheduler
import rx.Subscription

fun <T> bind(lifecycleObservable: Observable<ConductorLifecycle>, mainThreadScheduler: Scheduler, state: BehaviorRelay<T>, doView: (T) -> Unit): Subscription =
        lifecycleObservable
                .filter { it == ConductorLifecycle.Attach }
                .takeUntil { it == ConductorLifecycle.Detach }
                .flatMap { state }
                .observeOn(mainThreadScheduler)
                .subscribe(doView)