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

import android.view.ViewGroup;
import android.widget.TextView;

import com.pacoworks.dereference.widgets.BaseRecyclerAdapter;

import rx.functions.Func2;

public class ListExampleAdapter extends BaseRecyclerAdapter<String, ListExampleVH> {

    protected ListExampleAdapter() {
        super(new Func2<String, String, Boolean>() {
            @Override
            public Boolean call(String s, String s2) {
                return s.equals(s2);
            }
        });
    }

    @Override
    public ListExampleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListExampleVH(parent.getContext());
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    @Override
    protected void onBindViewHolder(ListExampleVH holder, int position, String element) {
        final TextView itemView = (TextView) holder.itemView;
        itemView.setText(position * 1000 + "");
    }
}