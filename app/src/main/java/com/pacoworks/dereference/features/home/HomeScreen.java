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

package com.pacoworks.dereference.features.home;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.home.model.Transaction;
import com.pacoworks.dereference.features.home.services.HomeAgotServiceKt;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function1;
import rx.Observable;

public class HomeScreen extends BaseController implements HomeView {

    private PublishRelay<String> userPublishRelay = PublishRelay.create();
    private final HomeState state;

    public HomeScreen() {
        super();
        state = new HomeState();
        HomeInteractorKt.subscribeHomeInteractor(this, state, new Function1<String, Observable<Transaction>>() {
            @Override
            public Observable<Transaction> invoke(String user) {
                return HomeAgotServiceKt.requestCharacterInfo(user);
            }
        });
    }

    @NonNull
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        final EditText textView = new EditText(container.getContext());
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userPublishRelay.call(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return textView;
    }

    @Override
    protected void attachBinders() {
        HomeInteractorKt.bindHomeInteractor(this, state);
    }

    public void setTitle(@NonNull String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void setLoading(@NotNull String user) {
        setTitle("Loading " + user);
        getView().setEnabled(false);
    }

    @Override
    public void showError(@NotNull String reason) {
        setTitle("Error");
        Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();
        getView().setEnabled(false);
    }

    @Override
    public void setWaiting(int seconds) {
        setTitle("Reloading in " + seconds);
        getView().setEnabled(false);
    }

    @Override
    public void showRepos(@NotNull String value) {
        setTitle("Hello " + value);
        getView().setEnabled(true);
    }

    @NotNull
    @Override
    public Observable<String> enterUser() {
        return userPublishRelay;
    }
}
