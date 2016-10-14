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

package com.pacoworks.dereference.architecture.reactive

/**
 * Algebra representing all possible results from an Activity request
 */
sealed class ActivityResult(open val requestCode: Int) {
    /**
     * Data class representing a successful request containing additional data
     */
    data class SuccessWithData(override val requestCode: Int, val data: Map<String, Any>) : ActivityResult(requestCode)

    /**
     * Data class representing a failed request containing additional data
     */
    data class FailureWithData(override val requestCode: Int, val data: Map<String, Any>) : ActivityResult(requestCode)

    /**
     * Data class representing a successful request
     */
    data class Success(override val requestCode: Int) : ActivityResult(requestCode)

    /**
     * Data class representing a failed request
     */
    data class Failure(override val requestCode: Int) : ActivityResult(requestCode)
}