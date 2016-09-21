package com.pacoworks.dereference;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.pacoworks.dereference.features.Home;
import com.pacoworks.dereference.features.Navigator;
import com.pacoworks.dereference.features.home.HomeScreen;
import com.pacoworks.rxsealedunions.Union0;

import org.jetbrains.annotations.NotNull;

import rx.functions.Func1;

public class MainNavigator implements Navigator {
    private final Router router;

    public MainNavigator(Router router) {
        this.router = router;
    }

    @Override
    public void goTo(@NotNull Union0<Home> screenUnion) {
        Controller screen = getScreen(screenUnion);
        router.pushController(RouterTransaction.with(screen)
                .pushChangeHandler(new FadeChangeHandler(false))
                .popChangeHandler(new FadeChangeHandler()));
    }

    private Controller getScreen(Union0<Home> screenUnion) {
        return screenUnion.join(new Func1<Home, Controller>() {
            @Override
            public Controller call(Home home) {
                return new HomeScreen();
            }
        });
    }

    @Override
    public void goBack() {
        router.popCurrentController();
    }
}
