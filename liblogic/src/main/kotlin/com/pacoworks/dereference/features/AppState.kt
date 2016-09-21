package com.pacoworks.dereference.features

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.core.ui.Direction
import com.pacoworks.dereference.core.ui.createHome
import com.pacoworks.dereference.core.ui.Screen
import org.javatuples.Pair

class AppState(
    val navigation: BehaviorRelay<org.javatuples.Pair<Screen, Direction>> = BehaviorRelay.create(Pair.with(createHome(), Direction.FORWARD))
)