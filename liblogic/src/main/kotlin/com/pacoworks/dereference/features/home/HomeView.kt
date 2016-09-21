package com.pacoworks.dereference.features.home

import com.pacoworks.dereference.core.reactive.None
import com.pacoworks.dereference.core.ui.BaseView
import rx.Observable

interface HomeView: BaseView {
    fun setTitle(title: String)

    fun clicks(): Observable<None>
}