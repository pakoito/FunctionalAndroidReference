package com.pacoworks.dereference.features

interface Navigator {
    fun goTo(screen: Screen)

    fun goBack()
}