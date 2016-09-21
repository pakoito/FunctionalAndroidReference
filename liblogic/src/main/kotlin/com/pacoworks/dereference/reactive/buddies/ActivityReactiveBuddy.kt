package com.pacoworks.dereference.reactive.buddies

import com.pacoworks.dereference.reactive.ActivityLifecycle
import com.pacoworks.dereference.reactive.ActivityResult
import com.pacoworks.dereference.reactive.Ignore
import com.pacoworks.dereference.reactive.PermissionResult
import rx.Observable

interface ActivityReactiveBuddy {
    fun lifecycle(): Observable<ActivityLifecycle>

    fun activityResult(): Observable<ActivityResult>

    fun permissionResult(): Observable<PermissionResult>

    fun back(): Observable<Ignore>
}