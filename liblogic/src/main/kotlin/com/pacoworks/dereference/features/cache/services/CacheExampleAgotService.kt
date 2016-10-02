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

package com.pacoworks.dereference.features.cache.services

import com.pacoworks.dereference.features.cache.model.AgotCharacter
import com.pacoworks.dereference.features.cache.model.createKnownCharacter
import com.pacoworks.dereference.features.cache.model.createUnknownNetworkErrorCharacter
import com.pacoworks.dereference.features.cache.model.createUnknownUnavailableCharacter
import com.pacoworks.dereference.network.AgotApi
import rx.Observable
import rx.Scheduler
import java.util.concurrent.TimeUnit


fun characterInfo(id: String, agotApi: AgotApi, scheduler: Scheduler): Observable<AgotCharacter> =
        agotApi.getCharacterInfo(id)
                .subscribeOn(scheduler)
                .flatMap {
                    it.name.let { name ->
                        Observable.just(
                                if (name != null) {
                                    createKnownCharacter(id, name, it.titles)
                                } else {
                                    createUnknownUnavailableCharacter(id)
                                })
                    }
                }
                .doOnError { System.out.println("FUCK: " + it.message) }
                .onErrorResumeNext(Observable.just<AgotCharacter>(createUnknownNetworkErrorCharacter(id)))
                /* Delay for testing purposes */
                .delay(2, TimeUnit.SECONDS)
