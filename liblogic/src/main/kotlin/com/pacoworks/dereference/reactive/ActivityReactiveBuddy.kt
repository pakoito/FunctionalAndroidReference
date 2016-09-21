package com.pacoworks.dereference.reactive

import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.Relay
import com.pacoworks.dereference.reactive.Lifecycle.*

interface ActivityReactiveBuddy {
    fun lifecycle(): Relay<Lifecycle, Lifecycle>
    fun activityResult(): Relay<ActivityResult, ActivityResult>
    fun permissionResult(): Relay<PermissionResult, PermissionResult>
}

class ReactiveActivity {

    val lifecycleRelay = BehaviorRelay.create<Lifecycle>()

    val activityResultRelay = BehaviorRelay.create<ActivityResult>()

    val permissionResultRelay = BehaviorRelay.create<PermissionResult>()

    private fun call(lifecycle: Lifecycle) = lifecycleRelay.call(lifecycle)

    fun onCreate() = call(Create)

    fun onStart() = call(Start)

    fun onResume() = call(Resume)

    fun onPause() = call(Pause)

    fun onStop() = call(Stop)

    fun onDestroy() = call(Destroy)

    fun onFinish() = call(Finish)

    fun onActivityResult(result: ActivityResult) = activityResultRelay.call(result)

    fun createBuddy() = object : ActivityReactiveBuddy {
        override fun lifecycle() = lifecycleRelay

        override fun activityResult() = activityResultRelay

        override fun permissionResult() = permissionResultRelay
    }
}