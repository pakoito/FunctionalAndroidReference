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

package com.pacoworks.dereference.features.home.services

import com.pacoworks.dereference.architecture.functional.Result
import com.pacoworks.dereference.features.home.model.Repository
import com.pacoworks.dereference.features.home.model.Transaction
import com.pacoworks.dereference.model.RepositoryDto
import com.pacoworks.dereference.network.createGithubApi
import com.pacoworks.rxcomprehensions.RxComprehensions
import rx.Notification
import rx.Observable

fun requestRepositoriesForUser(user: String): Observable<Transaction> =
        RxComprehensions.doFM(
                { createGithubApi().listRepos(user).materialize() },
                { result: Notification<List<RepositoryDto>> ->
                    Observable.just(when (result.kind) {
                        Notification.Kind.OnNext -> validate(result.value)
                        Notification.Kind.OnError -> Transaction.Failure(result.throwable.message!!)
                        else -> Transaction.Failure("Completed without results")
                    })
                }
        )

private fun validate(value: List<RepositoryDto>): Transaction =
        Observable.from(value)
                .collect({ Result.Success(mutableListOf<Repository>()) }, {
                    accumulator: Result<MutableList<Repository>>, value ->
                    when (accumulator) {
                        is Result.Failure -> accumulator
                        is Result.Success -> convert(accumulator.value, value)
                    }
                })
                .map { result: Result<MutableList<Repository>> ->
                    when (result) {
                        is Result.Failure -> Transaction.Failure("Repositories could not be validated")
                        is Result.Success -> Transaction.Success(result.value.toList())
                    }
                }.toBlocking().first()

private fun convert(elements: MutableList<Repository>, addition: RepositoryDto) =
        if (addition.id != 0L && addition.fullName != null) {
            Result.Success(elements.add(Repository(addition.id, addition.fullName!!)))
        } else {
            Result.Failure
        }