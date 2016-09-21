package com.pacoworks.dereference.core.reactive.buddies

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.reactive.ActivityLifecycle
import com.pacoworks.dereference.core.reactive.ActivityResult
import com.pacoworks.dereference.core.reactive.None
import com.pacoworks.dereference.core.reactive.PermissionResult

class ReactiveActivity {

    val lifecycleRelay = BehaviorRelay.create<ActivityLifecycle>()

    val activityResultRelay = BehaviorRelay.create<ActivityResult>()

    val permissionResultRelay = BehaviorRelay.create<PermissionResult>()

    val onBackRelay = BehaviorRelay.create<None>()

    private fun call(lifecycle: ActivityLifecycle) = lifecycleRelay.call(lifecycle)

    fun onEnter() = call(ActivityLifecycle.Enter)

    fun onCreate() = call(ActivityLifecycle.Create)

    fun onStart() = call(ActivityLifecycle.Start)

    fun onResume() = call(ActivityLifecycle.Resume)

    fun onPause() = call(ActivityLifecycle.Pause)

    fun onStop() = call(ActivityLifecycle.Stop)

    fun onDestroy() = call(ActivityLifecycle.Destroy)

    fun onFinish() = call(ActivityLifecycle.Exit)

    fun onActivityResult(result: ActivityResult) = activityResultRelay.call(result)

    fun onBackPressed() = onBackRelay.call(None.VOID)

    fun createBuddy() = object : ActivityReactiveBuddy {
        override fun lifecycle() = lifecycleRelay.asObservable()

        override fun activityResult() = activityResultRelay.asObservable()

        override fun permissionResult() = permissionResultRelay.asObservable()

        override fun back() = onBackRelay.asObservable()
    }
}