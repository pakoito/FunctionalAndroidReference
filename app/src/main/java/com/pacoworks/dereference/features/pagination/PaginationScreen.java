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

package com.pacoworks.dereference.features.pagination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.core.functional.None;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.list.ListExampleAdapter;
import com.pacoworks.dereference.features.pagination.services.PaginationExampleServiceKt;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.functions.Function1;
import rx.Observable;

/**
 * Controller implementing PaginationExampleView
 */
public class PaginationScreen extends BaseController implements PaginationExampleView {
    private static final int THRESHOLD = 5;

    private final PublishRelay<None> endOfPagePRelay = PublishRelay.create();

    private final PaginationExampleState state;

    private RecyclerView recyclerView;

    public PaginationScreen() {
        super();
        state = new PaginationExampleState();
        PaginationExampleInteractorKt.subscribePaginationExample(this, state, new Function1<Integer, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> invoke(Integer integer) {
                return PaginationExampleServiceKt.requestMore(integer);
            }
        });
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        recyclerView = new RecyclerView(context);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    private int previousTotalItemCount = 0;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (totalItemCount < previousTotalItemCount) {
                            this.previousTotalItemCount = totalItemCount;
                        }
                        if (totalItemCount > previousTotalItemCount) {
                            previousTotalItemCount = totalItemCount;
                        }
                        if ((pastVisiblesItems + visibleItemCount + THRESHOLD) >= totalItemCount) {
                            endOfPagePRelay.call(None.INSTANCE);
                        }
                    }
                }
        );
        final ListExampleAdapter adapter = new ListExampleAdapter();
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    protected void attachBinders() {
        PaginationExampleInteractorKt.bindPaginationExample(this, state);
    }

    @Override
    public void updateElements(@NotNull List<String> elements) {
        final ListExampleAdapter castedAdapter = getCastedAdapter();
        castedAdapter.swap(elements);
    }

    private ListExampleAdapter getCastedAdapter() {
        return (ListExampleAdapter) recyclerView.getAdapter();
    }

    @NotNull
    @Override
    public Observable<None> endOfPage() {
        return endOfPagePRelay.asObservable();
    }
}
