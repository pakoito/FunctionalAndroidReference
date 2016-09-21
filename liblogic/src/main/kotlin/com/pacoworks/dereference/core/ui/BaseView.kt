package com.pacoworks.dereference.core.ui

import com.jakewharton.rxrelay.BehaviorRelay
import rx.functions.Action2

interface BaseView {
    fun <T> createBinder(): Action2<BehaviorRelay<T>, (T) -> Unit>
}