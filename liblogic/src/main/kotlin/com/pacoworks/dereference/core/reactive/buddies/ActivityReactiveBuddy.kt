package com.pacoworks.dereference.core.reactive.buddies

import com.pacoworks.dereference.core.reactive.ActivityLifecycle
import com.pacoworks.dereference.core.reactive.ActivityResult
import com.pacoworks.dereference.core.reactive.None
import com.pacoworks.dereference.core.reactive.PermissionResult
import rx.Observable

interface ActivityReactiveBuddy {
    fun lifecycle(): Observable<ActivityLifecycle>

    fun activityResult(): Observable<ActivityResult>

    fun permissionResult(): Observable<PermissionResult>

    fun back(): Observable<None>
}