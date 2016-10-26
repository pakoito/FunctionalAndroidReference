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

package com.pacoworks.dereference.features.rotation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.global.DereferenceApplication;
import com.pacoworks.dereference.features.rotation.model.Transaction;
import com.pacoworks.dereference.features.rotation.services.RotationAgotServiceKt;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Controller implementing RotationView
 */
public class RotationScreen extends BaseController implements RotationView {

    private final PublishRelay<String> userPublishRelay = PublishRelay.create();

    private final RotationState state;

    private TextView textView;
    private EditText inputView;

    public RotationScreen() {
        super();
        state = new RotationState();
        RotationInteractorKt.subscribeRotationInteractor(createBuddy().lifecycle(), this, state, new Function1<String, Observable<Transaction>>() {
            @Override
            public Observable<Transaction> invoke(String user) {
                return RotationAgotServiceKt
                        .requestCharacterInfo(
                                user,
                                DereferenceApplication.get(getActivity()).getInjector().getAgotApi(),
                                Schedulers.newThread());
            }
        });
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(R.layout.screen_rotation, container, false);
        inputView = (EditText) view.findViewById(R.id.screen_rotation_edit);
        inputView.addTextChangedListener(new TextWatcher() {
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
        textView = (TextView) view.findViewById(R.id.screen_rotation_text);
        return view;
    }

    @Override
    protected void attachBinders() {
        RotationInteractorKt.bindRotationInteractor(this, state);
    }

    public void setText(@NonNull String text) {
        textView.setText(text);
    }

    @Override
    public void setLoading(@NotNull String user) {
        setText("Loading " + user);
        inputView.setEnabled(false);
        inputView.setText("");
    }

    @Override
    public void showError(@NotNull String reason) {
        setText("Error");
        Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();
        inputView.setEnabled(false);
    }

    @Override
    public void setWaiting(int seconds) {
        setText("Reloading in " + seconds);
        inputView.setEnabled(false);
    }

    @Override
    public void showCharacter(@NotNull String value) {
        setText(value);
        inputView.setEnabled(true);
    }

    @NotNull
    @Override
    public Observable<String> enterUser() {
        return userPublishRelay;
    }
}
