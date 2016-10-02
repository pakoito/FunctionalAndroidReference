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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.architecture.navigation.Direction;
import com.pacoworks.dereference.architecture.navigation.Home;
import com.pacoworks.dereference.architecture.navigation.ListExample;
import com.pacoworks.dereference.architecture.navigation.RotationExample;
import com.pacoworks.dereference.core.functional.Mapper;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.global.DereferenceApplication;
import com.pacoworks.dereference.features.home.model.HomeScreenSelection;
import com.pacoworks.rxsealedunions.Union3;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class HomeScreen extends BaseController implements HomeView {

    private final PublishRelay<HomeScreenSelection> screenSelectionPublishRelay = PublishRelay.create();

    public HomeScreen() {
        super();
        final Lazy<BehaviorRelay<Pair<Union3<Home, RotationExample, ListExample>, Direction>>> navigationLazy =
                LazyKt.lazy(new Function0<BehaviorRelay<Pair<Union3<Home, RotationExample, ListExample>, Direction>>>() {
                    @Override
                    public BehaviorRelay<Pair<Union3<Home, RotationExample, ListExample>, Direction>> invoke() {
                        return DereferenceApplication.get(getActivity()).getInjector().getState().getNavigation();
                    }
                });
        HomeInteractorKt.subscribeHomeInteractor(this, new Action1<Pair<Union3<Home, RotationExample, ListExample>, Direction>>() {
            @Override
            public void call(Pair<Union3<Home, RotationExample, ListExample>, Direction> navigation) {
                navigationLazy.getValue().call(navigation);
            }
        });
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        LinearLayout elements = new LinearLayout(context);
        elements.setOrientation(LinearLayout.VERTICAL);
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "RecyclerView", Mapper.<HomeScreenSelection>just(HomeScreenSelection.RecyclerView.INSTANCE)));
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "Rotation", Mapper.<HomeScreenSelection>just(HomeScreenSelection.Rotation.INSTANCE)));
        return elements;
    }

    private View createButton(Context context, PublishRelay<HomeScreenSelection> screenSelectionPublishRelay, String name, Func1<? super Object, HomeScreenSelection> func0) {
        TextView txv = new TextView(context);
        txv.setText(name);
        RxView.clicks(txv).map(func0).subscribe(screenSelectionPublishRelay);
        txv.setPadding(0, 10, 0, 10);
        return txv;
    }

    @Override
    protected void attachBinders() {

    }

    @NotNull
    @Override
    public Observable<HomeScreenSelection> buttonClick() {
        return screenSelectionPublishRelay;
    }
}
