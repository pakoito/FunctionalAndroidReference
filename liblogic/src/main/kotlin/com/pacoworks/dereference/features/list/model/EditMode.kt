/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.dereference.features.list.model

import com.pacoworks.rxsealedunions.Union2
import com.pacoworks.rxsealedunions.generic.GenericUnions

/**
 * Algebra to represent the current edit mode
 */
typealias EditMode = Union2<Normal, Delete>

private val EDIT_MODE_FACTORY: Union2.Factory<Normal, Delete> = GenericUnions.doubletFactory()

sealed class EditModes

/**
 * Data class to represent non-edit mode
 */
object Normal : EditModes()

/**
 * Data class to represent edit mode
 */
data class Delete(val id: String) : EditModes()

/**
 * Constructor for normal mode state
 */
fun createEditModeNormal(): EditMode =
        EDIT_MODE_FACTORY.first(Normal)

/**
 * Constructor for edit mode state
 */
fun createEditModeDelete(id: String): EditMode =
        EDIT_MODE_FACTORY.second(Delete(id))
