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

package com.pacoworks.dereference.features.rotation.services

import com.pacoworks.dereference.features.rotation.model.BookCharacter
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.model.agot.ToonDto
import com.pacoworks.dereference.network.AgotApi
import com.pacoworks.rxcomprehensions.RxComprehensions
import rx.Notification
import rx.Observable
import java.util.concurrent.TimeUnit

fun requestCharacterInfo(user: String, agotApi: AgotApi): Observable<Transaction> =
        RxComprehensions.doFM(
                {
                    agotApi.getCharacterInfo(user)
                            .materialize()
                            .filter { it.kind != Notification.Kind.OnCompleted }
                },
                { result: Notification<ToonDto> ->
                    Observable.just(when (result.kind) {
                        Notification.Kind.OnNext -> validate(result.value)
                        Notification.Kind.OnError ->
                            Transaction.Failure(
                                    if (result.throwable?.message == null) {
                                        ""
                                    } else {
                                        result.throwable.message!!
                                    })
                        else -> Transaction.Failure("Completed without results")
                    })
                })
                /* Add fake delay to better test rotation */
                .delay(5, TimeUnit.SECONDS)

private fun validate(value: ToonDto): Transaction =
        value.name.let {
            if (it == null) {
                Transaction.Failure("Character could not be validated")
            } else {
                Transaction.Success(BookCharacter(it))
            }
        }