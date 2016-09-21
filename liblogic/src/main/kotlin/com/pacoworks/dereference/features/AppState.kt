package com.pacoworks.dereference.features

import com.jakewharton.rxrelay.BehaviorRelay

class AppState {
    val navigation: BehaviorRelay<Screen> = BehaviorRelay.create(createHome())
}