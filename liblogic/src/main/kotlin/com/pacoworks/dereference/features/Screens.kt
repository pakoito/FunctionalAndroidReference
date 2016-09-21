package com.pacoworks.dereference.features

import com.pacoworks.rxsealedunions.Union0
import com.pacoworks.rxsealedunions.generic.GenericUnions

typealias Screen = Union0<Home>

val SCREEN_FACTORY: Union0.Factory<Home> = GenericUnions.nulletFactory<Home>()

sealed class Screens(open val id: String = "")
data class None(override val id: String = ""): Screens(id)

data class Home(override val id: String = ""): Screens(id)

fun createHome(): Union0<Home> = SCREEN_FACTORY.first(Home())