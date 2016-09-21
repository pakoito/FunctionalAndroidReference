package com.pacoworks.dereference.features.home

import com.jakewharton.rxrelay.BehaviorRelay

val title: BehaviorRelay<String> = BehaviorRelay.create<String>("Hello Paco")