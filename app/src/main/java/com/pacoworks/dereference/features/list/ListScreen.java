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

package com.pacoworks.dereference.features.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.core.functional.Mapper;
import com.pacoworks.dereference.core.functional.None;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.list.model.Delete;
import com.pacoworks.dereference.features.list.model.Normal;
import com.pacoworks.rxsealedunions.Union2;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;

public class ListScreen extends BaseController implements ListExampleView {
    private static final int SPAN_COUNT = 3;

    private final PublishRelay<Pair<Integer, String>> clicksPRelay = PublishRelay.create();

    private final PublishRelay<Pair<Integer, String>> longClicksPRelay = PublishRelay.create();

    private final PublishRelay<Pair<Integer, Integer>> dragAndDropPRelay = PublishRelay.create();

    private final PublishRelay<None> addPRelay = PublishRelay.create();

    private final PublishRelay<None> deletePRelay = PublishRelay.create();

    private final ListExampleState state;

    private RecyclerView recyclerView;

    private TextView deleteButton;

    private TextView addButton;

    public ListScreen() {
        super();
        state = new ListExampleState();
        ListExampleInteractorKt.subscribeListExampleInteractor(this, state);
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        final View inflate = inflater.inflate(R.layout.screen_list, container, false);
        addButton = (TextView) inflate.findViewById(R.id.screen_list_add);
        RxView.clicks(addButton).map(Mapper.<None>just(None.VOID)).subscribe(addPRelay);
        deleteButton = (TextView) inflate.findViewById(R.id.screen_list_delete);
        RxView.clicks(deleteButton).map(Mapper.<None>just(None.VOID)).subscribe(deletePRelay);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.screen_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
        final ListExampleAdapter adapter = new ListExampleAdapter();
        adapter.getClicks().subscribe(clicksPRelay);
        adapter.getLongClicks().subscribe(longClicksPRelay);
        recyclerView.setAdapter(adapter);
        return inflate;
    }

    @Override
    protected void attachBinders() {
        ListExampleInteractorKt.bindListExample(this, state);
    }

    @NotNull
    @Override
    public Observable<Pair<Integer, String>> listClicks() {
        return clicksPRelay.asObservable();
    }

    @NotNull
    @Override
    public Observable<Pair<Integer, String>> listLongClicks() {
        return longClicksPRelay.asObservable();
    }

    @NotNull
    @Override
    public Observable<Pair<Integer, Integer>> dragAndDropMoves() {
        return dragAndDropPRelay.asObservable();
    }

    @Override
    public void updateElements(@NotNull List<String> elements) {
        getCastedAdapter().swap(elements);
    }

    @Override
    public void updateSelected(@NotNull Set<String> elements) {
        getCastedAdapter().swapSelected(elements);
    }

    private ListExampleAdapter getCastedAdapter() {
        return (ListExampleAdapter) recyclerView.getAdapter();
    }

    @NotNull
    @Override
    public Observable<None> addClick() {
        return addPRelay.asObservable();
    }

    @NotNull
    @Override
    public Observable<None> deleteClick() {
        return deletePRelay.asObservable();
    }

    @Override
    public void updateEditMode(@NotNull Union2<Normal, Delete> editMode) {
        editMode.continued(new Action1<Normal>() {
            @Override
            public void call(Normal normal) {
                addButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
            }
        }, new Action1<Delete>() {
            @Override
            public void call(Delete delete) {
                addButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
