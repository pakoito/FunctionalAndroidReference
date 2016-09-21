package com.pacoworks.dereference.reactive.buddies

import com.pacoworks.dereference.reactive.ConductorLifecycle
import rx.Observable

interface ControllerReactiveBuddy {
    fun lifecycle(): Observable<ConductorLifecycle>
}

