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

package com.pacoworks.dereference.features.draganddrop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.list.ListExampleAdapter;
import com.pacoworks.dereference.features.list.ListExampleState;
import com.pacoworks.dereference.widgets.ReactiveDNDTouchHelper;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Controller implementing DragAndDropView
 */
public class DragAndDropScreen extends BaseController implements DragAndDropView {
    private static final int SPAN_COUNT = 3;

    private final PublishRelay<Pair<Integer, Integer>> dragAndDropPRelay = PublishRelay.create();

    private final ListExampleState state;

    private final PublishRelay<Pair<Integer, String>> listClicks = PublishRelay.create();

    private RecyclerView recyclerView;

    public DragAndDropScreen() {
        super();
        state = new ListExampleState();
        DragAndDropExampleInteractorKt.subscribeDragAndDropInteractor(this, state);
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
        final ListExampleAdapter adapter = new ListExampleAdapter();
        recyclerView.setAdapter(adapter);
        adapter.getClicks().subscribe(listClicks);
        final ReactiveDNDTouchHelper callback = new ReactiveDNDTouchHelper();
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        callback.getDNDObservable().subscribe(dragAndDropPRelay);
        touchHelper.attachToRecyclerView(recyclerView);
        return recyclerView;
    }

    @Override
    protected void attachBinders() {
        DragAndDropExampleInteractorKt.bindDragAndDropExample(this, state);
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
    public void updateSelected(@NotNull Set<String> selected) {
        getCastedAdapter().swapSelected(selected);
    }

    @NotNull
    @Override
    public Observable<Pair<Integer, String>> listClicks() {
        return listClicks;
    }

    private ListExampleAdapter getCastedAdapter() {
        return (ListExampleAdapter) recyclerView.getAdapter();
    }
}
