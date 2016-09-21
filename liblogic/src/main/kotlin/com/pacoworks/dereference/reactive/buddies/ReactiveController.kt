package com.pacoworks.dereference.reactive.buddies

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.reactive.ConductorLifecycle

class ReactiveController {

    val lifecycleRelay = BehaviorRelay.create<ConductorLifecycle>()

    private fun call(lifecycle: ConductorLifecycle) = lifecycleRelay.call(lifecycle)

    fun onEnter() = call(ConductorLifecycle.Enter)

    fun onCreate() = call(ConductorLifecycle.Create)

    fun onAttach() = call(ConductorLifecycle.Attach)

    fun onDetach() = call(ConductorLifecycle.Detach)

    fun onDestroy() = call(ConductorLifecycle.Destroy)

    fun onFinish() = call(ConductorLifecycle.Exit)

    fun createBuddy() = object : ControllerReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()
    }
}