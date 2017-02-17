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

package com.pacoworks.dereference

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*


internal class MockClient : Interceptor {
    private val events = ArrayDeque<Any>()
    private val requests = ArrayDeque<Request>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        requests.addLast(request)

        val event = events.removeFirst()
        if (event is IOException) {
            throw event
        }
        if (event is RuntimeException) {
            throw event
        }
        if (event is Response.Builder) {
            val response = event
            return response.request(request).protocol(Protocol.HTTP_1_1).build()
        }
        throw IllegalStateException("Unknown event " + event::class.java)
    }

    fun enqueueResponse(response: Response.Builder) {
        events.addLast(response)
    }

    fun enqueueUnexpectedException(exception: RuntimeException) {
        events.addLast(exception)
    }

    fun enqueueIOException(exception: IOException) {
        events.addLast(exception)
    }

    fun takeRequest(): Request {
        return requests.removeFirst()
    }
}