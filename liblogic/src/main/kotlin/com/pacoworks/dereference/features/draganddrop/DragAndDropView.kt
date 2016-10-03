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

package com.pacoworks.dereference.features.draganddrop

import com.pacoworks.dereference.architecture.ui.BoundView
import org.javatuples.Pair
import rx.Observable

interface DragAndDropView : DragAndDropInputView, DragAndDropOutputView {
}

interface DragAndDropInputView : BoundView {
    fun updateElements(elements: List<String>): Unit
}

interface DragAndDropOutputView {
    fun dragAndDropMoves(): Observable<Pair<Int, Int>>
}

