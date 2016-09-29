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

package com.pacoworks.dereference.features.home

import com.jakewharton.rxrelay.BehaviorRelay
import com.pacoworks.dereference.features.home.model.Transaction
import com.pacoworks.dereference.features.home.model.Transaction.*
import com.pacoworks.dereference.features.home.model.UserInput
import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

fun bindHomeInteractor(view: HomeViewInput, state: HomeState) {
    view.createBinder<Transaction>().call(state.transaction, {
        when (it) {
            is Loading -> view.setLoading()
            is Failure -> view.showError(it.reason)
            is WaitingForRetry -> view.setWaiting(it.seconds)
            is Success -> view.showRepos(it.repositories)
        }
    })
}

fun subscribeHomeInteractor(view: HomeViewOutput, state: HomeState, services: (String) -> Observable<Transaction>) =
        CompositeSubscription(
                handleUserChanges(view, state.user),
                handleLoad(state.transaction, services),
                handleReload(state.user, state.transaction),
                handleRetryAfterError(state.user, state.transaction))

fun handleUserChanges(view: HomeViewOutput, user: BehaviorRelay<UserInput>): Subscription =
        view.enterUser()
                .debounce(1, TimeUnit.SECONDS)
                .flatMap {
                    if (it.length > 3) {
                        Observable.just(UserInput(it))
                    } else {
                        Observable.empty()
                    }
                }
                .subscribe(user)

private fun handleLoad(transaction: BehaviorRelay<Transaction>, services: (String) -> Observable<Transaction>): Subscription =
        transaction.ofType(Loading::class.java)
                .switchMap { services.invoke(it.user.name) }
                .subscribe(transaction)

private fun handleReload(user: Observable<UserInput>, transaction: BehaviorRelay<Transaction>): Subscription =
        doFM(
                { user },
                { transaction.filter { it is Success }.first() },
                /* If the user hasn't changed since the previous reload */
                { currentUser, trans ->
                    user.first().filter { it != currentUser }
                }
        )
                .map { Loading(it) }
                .subscribe(transaction)

private fun handleRetryAfterError(user: Observable<UserInput>, transaction: BehaviorRelay<Transaction>): Subscription =
        transaction
                .filter { it is Failure }
                .flatMap {
                    Observable.interval(1, TimeUnit.SECONDS)
                            .startWith(0)
                            .map { WaitingForRetry((countdown - it).toInt()) as Transaction }
                            /* Countdown plus initial value plus zero */
                            .take(countdown + 2)
                            .concatWith(user.first().map { Loading(it) })
                }
                .subscribe(transaction)

private const val countdown = 5