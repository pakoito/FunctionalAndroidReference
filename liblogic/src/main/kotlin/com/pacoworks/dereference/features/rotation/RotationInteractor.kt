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

import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle
import com.pacoworks.dereference.architecture.ui.StateHolder
import com.pacoworks.dereference.features.rotation.model.Transaction
import com.pacoworks.dereference.features.rotation.model.Transaction.*
import com.pacoworks.dereference.features.rotation.model.UserInput
import com.pacoworks.dereference.features.rotation.services.TransactionRequest
import com.pacoworks.rxcomprehensions.RxComprehensions.doFM
import com.pacoworks.rxcomprehensions.RxComprehensions.doSM
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * Binds the state of this use case to a [com.pacoworks.dereference.architecture.ui.BoundView]
 *
 * @see [com.pacoworks.dereference.architecture.ui.bind]
 */
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

/**
 * Subscribes all use cases in the file
 */
fun subscribeRotationInteractor(lifecycle: Observable<ControllerLifecycle>, view: RotationViewOutput, state: RotationState, services: TransactionRequest) =
        CompositeSubscription(
                handleUserInput(view, state.user),
                handleStart(state.user, state.transaction),
                handleLoad(state.transaction, services),
                handleReload(state.user, state.transaction),
                handleRetryAfterError(state.user, state.transaction, lifecycle)
        )

fun handleUserInput(view: RotationViewOutput, user: StateHolder<UserInput>): Subscription =
        doSM(
                { user },
                /* We use switchMap because only valid inputs will update the state,
                 * which means it may not be the first value out of the debounce */
                { view.enterUser().debounce(1, TimeUnit.SECONDS) },
                { oldUserInput, newUserInput ->
                    if (oldUserInput.name != newUserInput && newUserInput.isNotEmpty()) {
                        Observable.just(UserInput(newUserInput))
                    } else {
                        Observable.empty()
                    }
                }
        )
                .subscribe(user)

fun handleStart(user: Observable<UserInput>, transaction: StateHolder<Transaction>): Subscription =
        doSM(
                { transaction.ofType(Idle::class.java) },
                {
                    user.filter { it.name != "" }
                            .first()
                            .map { Loading(it) }
                }
        )
                .subscribe(transaction)

fun handleLoad(transaction: StateHolder<Transaction>, services: TransactionRequest): Subscription =
        transaction.ofType(Loading::class.java)
                .switchMap { services.invoke(it.user.name) }
                .subscribe(transaction)

fun handleReload(user: StateHolder<UserInput>, transaction: StateHolder<Transaction>): Subscription =
        doFM(
                { user },
                { transaction.filter { it is Success }.first() },
                { currentUser, trans ->
                    /* Skip the current value. New version will arrive later. */
                    user.skip(1).first()
                            /* If the user hasn't changed since the previous reload */
                            .filter { it != currentUser }
                            .map { Loading(it) }
                }
        ).subscribe(transaction)

fun handleRetryAfterError(user: StateHolder<UserInput>, transaction: StateHolder<Transaction>, lifecycle: Observable<ControllerLifecycle>): Subscription =
        transaction
                .filter { it is Failure }
                .flatMap {
                    Observable.interval(0, 1, TimeUnit.SECONDS)
                            .map { WaitingForRetry((COUNTDOWN - it - 1).toInt()) }
                            .startWith(WaitingForRetry(COUNTDOWN))
                            /* Take until value is 0 */
                            .takeUntil { it.seconds <= 0 }
                            /* We want the chain to continue as a Transaction */
                            .map { it as Transaction }
                            .concatWith(user.first().map { Loading(it) })
                            /* Stop with the retry upon exiting the screen */
                            .takeUntil(lifecycle.filter { it == ControllerLifecycle.Exit })
                }
                .subscribe(transaction)

private const val COUNTDOWN = 5