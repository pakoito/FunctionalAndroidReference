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

import com.pacoworks.dereference.features.cache.model.*
import com.pacoworks.dereference.model.agot.ToonDto
import com.pacoworks.dereference.network.AgotApi
import com.pacoworks.rxsealedunions.Union2
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers

class CacheExampleAgotServiceKtTest {
    @Test
    fun requestCharacter_serverError_ErrorCharacter() {
        val characterId = ""
        val agotApi: AgotApi = object : AgotApi {
            override fun getCharacterInfo(id: String): Observable<ToonDto> =
                    Observable.error(RuntimeException())
        }
        val testSubscriber = TestSubscriber<Union2<UnknownAgotCharacter, KnownAgotCharacter>>()
        characterInfo(characterId, agotApi, Schedulers.immediate()).subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(createUnknownNetworkErrorCharacter(characterId))
    }

    @Test
    fun requestCharacter_wrongResponse_InvalidCharacter() {
        val characterId = ""
        val agotApi: AgotApi = object : AgotApi {
            override fun getCharacterInfo(id: String): Observable<ToonDto> =
                    Observable.just(ToonDto())
        }
        val testSubscriber = TestSubscriber<Union2<UnknownAgotCharacter, KnownAgotCharacter>>()
        characterInfo(characterId, agotApi, Schedulers.immediate()).subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(createUnknownIncorrectCharacter(characterId))
    }

    @Test
    fun requestCharacter_correctResponse_ValidCharacter() {
        val characterId = ""
        val agotApi: AgotApi = object : AgotApi {
            override fun getCharacterInfo(id: String): Observable<ToonDto> =
                    Observable.just(ToonDto(name = characterId, titles = listOf(characterId)))
        }
        val testSubscriber = TestSubscriber<Union2<UnknownAgotCharacter, KnownAgotCharacter>>()
        characterInfo(characterId, agotApi, Schedulers.immediate()).subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)
        testSubscriber.assertValue(createKnownCharacter(characterId, characterId, listOf(characterId)))
    }

}