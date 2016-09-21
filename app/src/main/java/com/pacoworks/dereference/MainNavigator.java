package com.pacoworks.dereference;

import android.app.Activity;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.pacoworks.dereference.core.ui.Navigator;
import com.pacoworks.dereference.features.Home;
import com.pacoworks.dereference.features.ScreensKt;
import com.pacoworks.dereference.features.home.HomeScreen;
import com.pacoworks.rxsealedunions.Union0;
import com.pacoworks.rxsealedunions.Union1;
import com.pacoworks.rxsealedunions.generic.GenericUnions;

import org.jetbrains.annotations.NotNull;

import rx.functions.Func1;

public class MainNavigator implements Navigator {
    private static final Union1.Factory<Union0<Home>> BACK_RESULT_FACTORY = GenericUnions.singletFactory();

    private final Router router;

    private final Activity activity;

    public MainNavigator(Router router, Activity activity) {
        this.router = router;
        this.activity = activity;
    }

    @Override
    public void goTo(@NotNull Union0<Home> screenUnion) {
        Controller screen = getControllerFromScreen(screenUnion);
        router.pushController(RouterTransaction.with(screen)
                .pushChangeHandler(new FadeChangeHandler(false))
                .popChangeHandler(new FadeChangeHandler()));
    }

    @Override
    public Union1<Union0<Home>> goBack() {
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

    private Controller getControllerFromScreen(Union0<Home> screenUnion) {
        return screenUnion.join(new Func1<Home, Controller>() {
            @Override
            public Controller call(Home home) {
                return new HomeScreen();
            }
        });
    }

    private Union0<Home> getScreenFromController(Controller controller) {
        return ScreensKt.createHome();
    }
}
