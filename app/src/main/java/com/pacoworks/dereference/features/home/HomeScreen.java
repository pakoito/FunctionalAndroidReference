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
import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.SerializedRelay;
import com.pacoworks.dereference.architecture.navigation.CacheExample;
import com.pacoworks.dereference.architecture.navigation.Direction;
import com.pacoworks.dereference.architecture.navigation.DragAndDropExample;
import com.pacoworks.dereference.architecture.navigation.Home;
import com.pacoworks.dereference.architecture.navigation.ListExample;
import com.pacoworks.dereference.architecture.navigation.PaginationExample;
import com.pacoworks.dereference.architecture.navigation.RotationExample;
import com.pacoworks.dereference.architecture.navigation.ScreensKt;
import com.pacoworks.dereference.core.functional.Mapper;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.global.DereferenceApplication;
import com.pacoworks.rxsealedunions.Union6;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import rx.Observable;
import rx.functions.Func1;

public class HomeScreen extends BaseController implements HomeView {

    private final PublishRelay<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> screenSelectionPublishRelay = PublishRelay.create();

    public HomeScreen() {
        super();
        /* This is why Kotlin's typealias is important */
        final Lazy<SerializedRelay<Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>, Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>>> navigationLazy =
                LazyKt.lazy(new Function0<SerializedRelay<Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>, Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>>>() {
                    @Override
                    public SerializedRelay<Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>, Pair<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>, Direction>> invoke() {
                        return DereferenceApplication.get(getActivity()).getInjector().getState().getNavigation();
                    }
                });
        HomeInteractorKt.subscribeHomeInteractor(this, navigationLazy.getValue());
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        LinearLayout elements = new LinearLayout(context);
        elements.setOrientation(LinearLayout.VERTICAL);
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "RecyclerView with Delete mode", Mapper.<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>>just(ScreensKt.createList())));
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "Drag And Drop", Mapper.<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>>just(ScreensKt.createDragAndDrop())));
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "Rotation", Mapper.<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>>just(ScreensKt.createRotation())));
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "Cache", Mapper.<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>>just(ScreensKt.createCache())));
        elements.addView(createButton(container.getContext(), screenSelectionPublishRelay, "Pagination", Mapper.<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>>just(ScreensKt.createPagination())));
        return elements;
    }

    private View createButton(Context context, PublishRelay<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> screenSelectionPublishRelay, String name, Func1<? super Object, Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> func0) {
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
    public Observable<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> buttonClick() {
        return screenSelectionPublishRelay;
    }
}
