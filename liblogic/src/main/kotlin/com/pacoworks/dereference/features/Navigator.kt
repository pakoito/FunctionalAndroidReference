package com.pacoworks.dereference.features

import com.pacoworks.rxsealedunions.Union1

interface Navigator {
    fun goTo(screen: Screen)

    fun goBack(): Union1<Screen>
}