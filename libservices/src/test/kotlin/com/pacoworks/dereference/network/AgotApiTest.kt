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

import com.pacoworks.dereference.MockClient
import com.pacoworks.dereference.model.agot.ToonDto
import okhttp3.OkHttpClient
import org.junit.Test
import rx.observers.TestSubscriber
import java.io.IOException

class AgotApiTest {
    @Test
    fun crateApi_FakeError_SeenError() {
        val mockClient = MockClient()
        val okHttpClient = OkHttpClient.Builder().addInterceptor(mockClient).build()
        /* Fake error */
        mockClient.enqueueIOException(MyException())
        /* Create api*/
        val createAgotApi = createAgotApi(okHttpClient)
        /* Start request */
        val testSubscriber = TestSubscriber.create<ToonDto>()
        createAgotApi.getCharacterInfo("5").subscribe(testSubscriber)
        /* Assert error */
        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertError(MyException::class.java)
    }

}

private class MyException: IOException()