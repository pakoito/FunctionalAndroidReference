package com.pacoworks.dereference.core.ui

import com.pacoworks.dereference.features.Screen
import com.pacoworks.rxsealedunions.Union1

interface Navigator {
    fun goTo(screen: Screen)

    fun goBack(): Union1<Screen>
}