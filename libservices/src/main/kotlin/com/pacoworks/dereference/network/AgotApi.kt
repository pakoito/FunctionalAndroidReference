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

package com.pacoworks.dereference.network

import com.pacoworks.dereference.model.agot.ToonDto
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

interface AgotApi {
    @GET("characters/{id}/")
    fun getCharacterInfo(@Path("id") id: String): Observable<ToonDto>
}

private val RETROFIT_INSTANCE =
        Retrofit.Builder()
                .baseUrl("http://anapioficeandfire.com/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

fun createAgotApi(): AgotApi =
        RETROFIT_INSTANCE.create(AgotApi::class.java)