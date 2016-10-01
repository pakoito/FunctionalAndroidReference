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

package com.pacoworks.dereference.features.rotation

import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.Transaction.*
import com.pacoworks.dereference.features.rotation.model.UserInput
import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

fun bindRotationInteractor(view: RotationViewInput, state: RotationState) {
    view.createBinder<Transaction>().call(state.transaction, {
        when (it) {
            is Loading -> view.setLoading(it.user.name)
            is Failure -> view.showError(it.reason)
            is WaitingForRetry -> view.setWaiting(it.seconds)
            is Success -> view.showRepos(it.charInfo.name)
        }
    })
}

fun subscribeRotationInteractor(view: RotationViewOutput, state: RotationState, services: (String) -> Observable<Transaction>) =
        CompositeSubscription(
                handleUserInput(view, state.user),
                handleStart(state.user, state.transaction),
                handleLoad(state.transaction, services),
                handleReload(state.user, state.transaction),
                handleRetryAfterError(state.user, state.transaction)
        )

fun handleUserInput(view: RotationViewOutput, user: StateHolder<UserInput>): Subscription =
        view.enterUser()
                .debounce(1, TimeUnit.SECONDS)
                .switchMap {
                    if (it.length > 0) {
                        Observable.just(UserInput(it))
                    } else {
                        Observable.empty()
                    }
                }
                .subscribe(user)

private fun handleStart(user: Observable<UserInput>, transaction: StateHolder<Transaction>): Subscription =
        transaction.ofType(Idle::class.java)
                .first()
                .switchMap { user.first().map { Loading(it) } }
                .subscribe(transaction)

private fun handleLoad(transaction: StateHolder<Transaction>, services: (String) -> Observable<Transaction>): Subscription =
        transaction.ofType(Loading::class.java)
                .filter { it.user.name != "" }
                .switchMap { services.invoke(it.user.name) }
                .subscribe(transaction)

private fun handleReload(user: Observable<UserInput>, transaction: StateHolder<Transaction>): Subscription =
        doFM(
                { user },
                { transaction.filter { it is Success }.first() },
                /* If the user hasn't changed since the previous reload */
                { currentUser, trans ->
                    /* Skip the current value. New version will arrive later. */
                    user.skip(1).first().filter { it != currentUser }.map { Loading(it) }
                }
        )
                .subscribe(transaction)

private fun handleRetryAfterError(user: Observable<UserInput>, transaction: StateHolder<Transaction>): Subscription =
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