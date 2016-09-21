package com.pacoworks.dereference.features

import com.jakewharton.rxrelay.BehaviorRelay
import org.javatuples.Pair

class AppState(
    val navigation: BehaviorRelay<org.javatuples.Pair<Screen, Direction>> = BehaviorRelay.create(Pair.with(createHome(), Direction.FORWARD))
)