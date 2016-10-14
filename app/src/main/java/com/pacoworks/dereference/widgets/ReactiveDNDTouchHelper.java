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


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jakewharton.rxrelay.PublishRelay;

import org.javatuples.Pair;

import rx.Observable;
import rx.functions.Func1;

/**
 * Implementation of {@link ItemTouchHelper.Callback} exposing support for reactive operations, like observing drag and
 * drop operations on a list.
 */
public class ReactiveDNDTouchHelper extends ItemTouchHelper.Callback {
    private final PublishRelay<Integer> moveStartRelay = PublishRelay.create();

    private final PublishRelay<Pair<Integer, Integer>> movesRelay = PublishRelay.create();

    private final PublishRelay<Integer> moveEndRelay = PublishRelay.create();

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public Observable<Pair<Integer, Integer>> getDNDObservable() {
        return moveStartRelay
                .switchMap(new Func1<Integer, Observable<Pair<Integer, Integer>>>() {
                    @Override
                    public Observable<Pair<Integer, Integer>> call(Integer integer) {
                        return movesRelay.takeUntil(moveEndRelay);
                    }
                });
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT
                    | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else if (layoutManager instanceof LinearLayoutManager) {
            final int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            final int dragFlags;
            final int swipeFlags;
            if (orientation == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            } else {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            return makeMovementFlags(0, 0);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                          RecyclerView.ViewHolder target) {
        movesRelay.call(Pair.with(source.getAdapterPosition(), target.getAdapterPosition()));
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            moveStartRelay.call(viewHolder.getAdapterPosition());
            if (viewHolder instanceof ReactiveDNDViewHolder) {
                ((ReactiveDNDViewHolder) viewHolder).onDragStarted();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        moveEndRelay.call(viewHolder.getAdapterPosition());
        if (viewHolder instanceof ReactiveDNDViewHolder) {
            ((ReactiveDNDViewHolder) viewHolder).onDragEnded();
        }
    }

    /**
     * Interface to be implemented by ViewHolders to be animated during Drag and Drop operations.
     */
    public interface ReactiveDNDViewHolder {
        void onDragStarted();

        void onDragEnded();
    }
}

