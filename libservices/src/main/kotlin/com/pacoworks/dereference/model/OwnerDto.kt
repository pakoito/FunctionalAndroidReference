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

package com.pacoworks.dereference.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OwnerDto(
        @SerializedName("login")
        @Expose
        var login: String? = null,
        @SerializedName("id")
        @Expose
        var id: Long = 0,
        @SerializedName("avatar_url")
        @Expose
        var avatarUrl: String? = null,
        @SerializedName("gravatar_id")
        @Expose
        var gravatarId: String? = null,
        @SerializedName("url")
        @Expose
        var url: String? = null,
        @SerializedName("html_url")
        @Expose
        var htmlUrl: String? = null,
        @SerializedName("followers_url")
        @Expose
        var followersUrl: String? = null,
        @SerializedName("following_url")
        @Expose
        var followingUrl: String? = null,
        @SerializedName("gists_url")
        @Expose
        var gistsUrl: String? = null,
        @SerializedName("starred_url")
        @Expose
        var starredUrl: String? = null,
        @SerializedName("subscriptions_url")
        @Expose
        var subscriptionsUrl: String? = null,
        @SerializedName("organizations_url")
        @Expose
        var organizationsUrl: String? = null,
        @SerializedName("repos_url")
        @Expose
        var reposUrl: String? = null,
        @SerializedName("events_url")
        @Expose
        var eventsUrl: String? = null,
        @SerializedName("received_events_url")
        @Expose
        var receivedEventsUrl: String? = null,
        @SerializedName("type")
        @Expose
        var type: String? = null,
        @SerializedName("site_admin")
        @Expose
        var siteAdmin: Boolean = false

)