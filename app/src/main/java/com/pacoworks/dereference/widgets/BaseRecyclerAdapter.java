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

package com.pacoworks.dereference.widgets;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxrelay.PublishRelay;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public abstract class BaseRecyclerAdapter<T, U extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<U> {

    private final List<T> values = new ArrayList<>();

    private final PublishRelay<Pair<Integer, T>> clicksRelay = PublishRelay.create();

    private final PublishRelay<Pair<Integer, T>> longClicksRelay = PublishRelay.create();

    private final Func2<T, T, Boolean> hasSameId;

    protected BaseRecyclerAdapter(Func2<T, T, Boolean> hasSameId) {
        this.hasSameId = hasSameId;
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void swap(final List<T> newValues) {
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(createElementsDiffCallback(newValues), true);
        values.clear();
        values.addAll(newValues);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    private DiffUtil.Callback createElementsDiffCallback(final List<T> newValues) {
        return new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return values.size();
            }

            @Override
            public int getNewListSize() {
                return newValues.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return hasSameId.call(values.get(oldItemPosition), newValues.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return values.get(oldItemPosition).equals(newValues.get(newItemPosition));
            }
        };
    }

    @Override
    public void onBindViewHolder(final U holder, final int position) {
        final T element = values.get(position);
        RxView.clicks(holder.itemView).map(new Func1<Void, Pair<Integer, T>>() {
            @Override
            public Pair<Integer, T> call(Void aVoid) {
                return Pair.with(holder.getAdapterPosition(), element);
            }
        }).subscribe(clicksRelay);
        RxView.longClicks(holder.itemView).map(new Func1<Void, Pair<Integer, T>>() {
            @Override
            public Pair<Integer, T> call(Void aVoid) {
                return Pair.with(holder.getAdapterPosition(), element);
            }
        }).subscribe(longClicksRelay);
        onBindViewHolder(holder, position, element);
    }

    protected abstract void onBindViewHolder(U holder, int position, T element);

    protected T getPosition(int position) {
        return values.get(position);
    }

    public Observable<Pair<Integer, T>> getClicks() {
        return clicksRelay.asObservable();
    }

    public Observable<Pair<Integer, T>> getLongClicks() {
        return clicksRelay.asObservable();
    }
}
