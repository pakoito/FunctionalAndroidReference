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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.widgets.ReactiveDNDTouchHelper;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.functions.Func1;

public class ListScreen extends BaseController implements ListExampleView {

    private static final int SPAN_COUNT = 3;

    final PublishRelay<Pair<Integer, String>> clicksPRelay = PublishRelay.create();

    final PublishRelay<Pair<Integer, String>> longClicksPRelay = PublishRelay.create();

    final PublishRelay<Pair<Integer, Integer>> dragAndDropPRelay = PublishRelay.create();

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        final RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
        final ListExampleAdapter adapter = new ListExampleAdapter();
        adapter.swap(Observable.range(0, 1000).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer.toString();
            }
        }).toList().toBlocking().first());
        adapter.getClicks().subscribe(clicksPRelay);
        adapter.getLongClicks().subscribe(longClicksPRelay);
        recyclerView.setAdapter(adapter);
        final ReactiveDNDTouchHelper callback = new ReactiveDNDTouchHelper();
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        callback.getDNDObservable().subscribe(dragAndDropPRelay);
        touchHelper.attachToRecyclerView(recyclerView);
        return recyclerView;
    }

    @Override
    protected void attachBinders() {

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
}
