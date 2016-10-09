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

package com.pacoworks.dereference.features.global;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.pacoworks.dereference.architecture.navigation.CacheExample;
import com.pacoworks.dereference.architecture.navigation.DragAndDropExample;
import com.pacoworks.dereference.architecture.navigation.Home;
import com.pacoworks.dereference.architecture.navigation.ListExample;
import com.pacoworks.dereference.architecture.navigation.PaginationExample;
import com.pacoworks.dereference.architecture.navigation.RotationExample;
import com.pacoworks.dereference.architecture.navigation.ScreensKt;
import com.pacoworks.dereference.features.cache.CacheScreen;
import com.pacoworks.dereference.features.draganddrop.DragAndDropScreen;
import com.pacoworks.dereference.features.home.HomeScreen;
import com.pacoworks.dereference.features.list.ListScreen;
import com.pacoworks.dereference.features.pagination.PaginationScreen;
import com.pacoworks.dereference.features.rotation.RotationScreen;
import com.pacoworks.rxsealedunions.Union1;
import com.pacoworks.rxsealedunions.Union6;
import com.pacoworks.rxsealedunions.generic.GenericUnions;

import org.jetbrains.annotations.NotNull;

import rx.functions.Func1;

public class MainNavigator implements NavigatorView {
    private static final Union1.Factory<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> BACK_RESULT_FACTORY = GenericUnions.singletFactory();

    private final Router router;

    private final Activity activity;

    public MainNavigator(Router router, Activity activity) {
        this.router = router;
        this.activity = activity;
    }

    @Override
    public void goTo(@NotNull Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample> screenUnion) {
        Controller screen = getControllerFromScreen(screenUnion);
        router.pushController(RouterTransaction.with(screen)
                .pushChangeHandler(new FadeChangeHandler())
                .popChangeHandler(new FadeChangeHandler()));
    }

    @NonNull
    @Override
    public Union1<Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample>> goBack() {
        final int backstackSize = router.getBackstackSize();
        if (backstackSize > 1) {
            final RouterTransaction routerTransaction = router.getBackstack().get(backstackSize - 1);
            final Controller controller = routerTransaction.controller();
            router.popCurrentController();
            return BACK_RESULT_FACTORY.first(getScreenFromController(controller));
        } else {
            activity.finish();
            return BACK_RESULT_FACTORY.none();
        }
    }

    private Controller getControllerFromScreen(Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample> screenUnion) {
        return screenUnion.join(new Func1<Home, Controller>() {
            @Override
            public Controller call(Home home) {
                return new HomeScreen();
            }
        }, new Func1<RotationExample, Controller>() {
            @Override
            public Controller call(RotationExample rotationExample) {
                return new RotationScreen();
            }
        }, new Func1<ListExample, Controller>() {
            @Override
            public Controller call(ListExample listExample) {
                return new ListScreen();
            }
        }, new Func1<CacheExample, Controller>() {
            @Override
            public Controller call(CacheExample cacheExample) {
                return new CacheScreen();
            }
        }, new Func1<DragAndDropExample, Controller>() {
            @Override
            public Controller call(DragAndDropExample dragAndDropExample) {
                return new DragAndDropScreen();
            }
        }, new Func1<PaginationExample, Controller>() {
            @Override
            public Controller call(PaginationExample paginationExample) {
                return new PaginationScreen();
            }
        });
    }

    private Union6<Home, RotationExample, ListExample, CacheExample, DragAndDropExample, PaginationExample> getScreenFromController(Controller controller) {
        return ScreensKt.createHome();
    }
}
