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
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class ListExampleVH extends RecyclerView.ViewHolder {
    public ListExampleVH(Context context) {
        super(new TextView(context));
        TextView txv = (TextView) itemView;
        txv.setPadding(0, 20, 0, 20);
    }
}
