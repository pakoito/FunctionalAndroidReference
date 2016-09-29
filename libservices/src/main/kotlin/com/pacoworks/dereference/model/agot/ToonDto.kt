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

package com.pacoworks.dereference.model.agot

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ToonDto(
        @SerializedName("url")
        @Expose
        var url: String? = null,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("culture")
        @Expose
        var culture: String? = null,
        @SerializedName("born")
        @Expose
        var born: String? = null,
        @SerializedName("died")
        @Expose
        var died: String? = null,
        @SerializedName("titles")
        @Expose
        var titles: List<String> = ArrayList(),
        @SerializedName("aliases")
        @Expose
        var aliases: List<String> = ArrayList(),
        @SerializedName("father")
        @Expose
        var father: String? = null,
        @SerializedName("mother")
        @Expose
        var mother: String? = null,
        @SerializedName("spouse")
        @Expose
        var spouse: String? = null,
        @SerializedName("allegiances")
        @Expose
        var allegiances: List<String> = ArrayList(),
        @SerializedName("books")
        @Expose
        var books: List<String> = ArrayList(),
        @SerializedName("povBooks")
        @Expose
        var povBooks: List<String> = ArrayList(),
        @SerializedName("tvSeries")
        @Expose
        var tvSeries: List<String> = ArrayList(),
        @SerializedName("playedBy")
        @Expose
        var playedBy: List<String> = ArrayList()

)