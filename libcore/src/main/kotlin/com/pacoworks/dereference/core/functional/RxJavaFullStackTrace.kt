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

package com.pacoworks.dereference.core.functional

import rx.Observable
import rx.Subscriber
import rx.exceptions.Exceptions
import rx.functions.Func1

/**
 * Reimplementation of https://github.com/Cookizz/RxJavaStackTracer
 * using https://github.com/ReactiveX/RxJava/issues/3521#issuecomment-169375782 as operator
 */
class RxJavaFullStackTrace<T> : Observable.Operator<T, T> {

    private val asyncOriginStackTraceProvider = RuntimeException("Async origin")

    private val originThreadId = Thread.currentThread().id // should be "enough" unique. See http://stackoverflow.com/a/591664/1662412

    override fun call(child: Subscriber<in T>): Subscriber<in T> {
        val parent = object : Subscriber<T>() {

            override fun onCompleted() {
                child.onCompleted()
            }

            override fun onError(throwable: Throwable) {
                if (Thread.currentThread().id != originThreadId) {
                    val originalStackTraceElements = throwable.stackTrace
                    val additionalAsyncOriginStackTraceElements: List<StackTraceElement> =
                            asyncOriginStackTraceProvider.stackTrace
                                    .filter { RxJavaFullStackTrace::class.java.name != it.className }
                    val modifiedStackTraceElements = originalStackTraceElements + additionalAsyncOriginStackTraceElements
                    throwable.stackTrace = modifiedStackTraceElements
                }
                child.onError(clearNestedStack(throwable))
            }

            override fun onNext(t: T) {
                child.onNext(t)
            }
        }
        child.add(parent)
        return parent
    }


    companion object {
        @JvmStatic
        fun <T> traceOriginalThread(): Func1<Observable.OnSubscribe<out T>, Observable.OnSubscribe<out T>> =
                Func1 { onSubscribe ->
                    Observable.OnSubscribe<T> { o ->
                        try {
                            val operator = RxJavaFullStackTrace<T>()
                            val st = operator.call(o)
                            try {
                                st.onStart()
                                onSubscribe.call(st)
                            } catch (e: Throwable) {
                                Exceptions.throwIfFatal(e)
                                st.onError(e)
                            }

                        } catch (e: Throwable) {
                            Exceptions.throwIfFatal(e)
                            o.onError(e)
                        }
                    }
                }

        @JvmStatic
        fun clearNestedStack(throwable: Throwable): Throwable =
                throwable.apply {
                    stackTrace = stackTrace.filter { it.className.contains("^rx\\.") }.toTypedArray()
                    if (null != cause) {
                        clearNestedStack(cause!!)
                    }
                }
    }
}