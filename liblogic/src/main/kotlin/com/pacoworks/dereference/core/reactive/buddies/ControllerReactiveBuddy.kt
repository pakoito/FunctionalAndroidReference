package com.pacoworks.dereference.core.reactive.buddies

import com.pacoworks.dereference.core.reactive.ConductorLifecycle
import rx.Observable

interface ControllerReactiveBuddy {
    fun lifecycle(): Observable<ConductorLifecycle>
}

