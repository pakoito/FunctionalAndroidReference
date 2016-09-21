package com.pacoworks.dereference.features.home

import com.pacoworks.dereference.reactive.BaseView
import com.pacoworks.dereference.reactive.None
import rx.Observable

interface HomeView: BaseView {
    fun setTitle(title: String)

    fun clicks(): Observable<None>
}