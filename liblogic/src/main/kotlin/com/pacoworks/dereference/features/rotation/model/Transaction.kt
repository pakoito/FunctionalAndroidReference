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

package com.pacoworks.dereference.features.rotation.model

/**
 * Algebra to represent the state of a network transaction
 */
sealed class Transaction {
    /**
     * Data class to represent that the transaction is idle
     */
    object Idle : Transaction()

    /**
     * Data class to represent that the transaction is loading some user input
     */
    data class Loading(val user: UserInput) : Transaction()

    /**
     * Data class to represent that the transaction is complete and has retrieved a [BookCharacter]
     */
    data class Success(val charInfo: BookCharacter) : Transaction()

    /**
     * Data class to represent that the transaction has failed to complete along with the request id and reason
     */
    data class Failure(val reason: String, val request: UserInput) : Transaction()

    /**
     * Data class to represent that the transaction will be retried and how many seconds until it happens
     */
    data class WaitingForRetry(val seconds: Int) : Transaction()
}