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

package com.pacoworks.dereference.features.global;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.jakewharton.rxrelay.SerializedRelay;
import com.pacoworks.dereference.architecture.reactive.ControllerLifecycle;
import com.pacoworks.dereference.architecture.reactive.buddies.ControllerReactiveBuddy;
import com.pacoworks.dereference.architecture.reactive.buddies.ReactiveController;
import com.pacoworks.dereference.architecture.ui.BoundView;
import com.pacoworks.dereference.architecture.ui.ControllerBinderKt;
import com.pacoworks.rxpartialapplication.RxPartialAction;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Action3;

/**
 * Abstract base class for all Controllers, representing a single screen or visible element in the app. It delegates all its framework responsibilities to a proxy
 * class {@link ReactiveController}.
 * <p>
 * It implements the interface {@link BoundView} and provides a default implementation for constructing Android-lifecycle aware binders.
 * <p>
 * The Controller provides access to its own Android lifecycle state via a proxy class {@link ControllerReactiveBuddy}.
 * <p>
 * It defines its own lifecycle as three entry points:
 * <ul>
 * <li>A constructor {@link BaseController#BaseController()} or {@link BaseController#BaseController(Bundle)} to create the state models and start the business logic.
 * <li>A {@link #createView(Context, LayoutInflater, ViewGroup)} method to request the current View.
 * <li>A method {@link #attachBinders()} to start the binding between view and models.
 * </ul>
 */
public abstract class BaseController extends Controller implements BoundView {
    private final ReactiveController reactiveController = new ReactiveController();

    /**
     * Lifecycle entry point for logic.
     * <p>
     * Use to create the screen's state and initialize the business logic.
     */
    public BaseController() {
        super();
        reactiveController.onEnter();
    }

    /**
     * Lifecycle entry point for logic.
     * <p>
     * Use to create the screen's state and initialize the business logic.
     *
     * @param args arguments passed when this instance is created
     */
    public BaseController(Bundle args) {
        super(args);
        reactiveController.onEnter();
    }

    @NonNull
    @Override
    protected final View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        final View view = createView(container.getContext(), inflater, container);
        reactiveController.onCreate();
        return view;
    }

    /**
     * Lifecycle entry point for view.
     * <p>
     * Requests the view representing the current screen.
     * <p>
     * Use to initialize the view and its state: adapters, styling, listeners...
     *
     * @param context   current Android context
     * @param inflater  layout inflater based off the current context
     * @param container parent view
     * @return a new instance of the view attached to this screen
     */
    @NonNull
    @CheckResult
    protected abstract View createView(Context context, LayoutInflater inflater, ViewGroup container);

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        attachBinders();
        reactiveController.onAttach();
    }

    /**
     * Lifecycle entry point for binding state to the view.
     * <p>
     * Use to call any binding methods.
     */
    protected abstract void attachBinders();

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        reactiveController.onDetach();
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        reactiveController.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reactiveController.onFinish();
    }

    @Override
    @NonNull
    public <T> Action2<SerializedRelay<T, T>, Function1<T, Unit>> createBinder() {
        return RxPartialAction.apply(new Action3<Observable<ControllerLifecycle>, SerializedRelay<T, T>, Function1<T, Unit>>() {
            @Override
            public void call(Observable<ControllerLifecycle> lifecycle, SerializedRelay<T, T> state, Function1<T, Unit> view) {
                ControllerBinderKt.bind(lifecycle, AndroidSchedulers.mainThread(), state, view);
            }

        }, createBuddy().lifecycle());
    }

    /**
     * Creates a proxy object {@link ControllerReactiveBuddy} to access controller-related events, like lifecycle.
     *
     * @return a new {@link ControllerReactiveBuddy}
     */
    protected ControllerReactiveBuddy createBuddy() {
        return reactiveController.createBuddy();
    }
}
