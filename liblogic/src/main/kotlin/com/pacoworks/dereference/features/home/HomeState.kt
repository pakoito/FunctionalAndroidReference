package com.pacoworks.dereference.features.home

import com.jakewharton.rxrelay.BehaviorRelay

class HomeState(
        val counter: BehaviorRelay<Int> = BehaviorRelay.create(1)
)