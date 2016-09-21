package com.pacoworks.dereference.features.global;

import com.pacoworks.dereference.core.reactive.buddies.ActivityReactiveBuddy;
import com.pacoworks.dereference.core.ui.Navigator;

import rx.android.schedulers.AndroidSchedulers;

public class MainOrchestrator {

    public static void start(AppState state, Navigator navigator, ActivityReactiveBuddy activityReactiveBuddy) {
        NavigationInteractorKt.subscribeNavigation(state, navigator, activityReactiveBuddy, AndroidSchedulers.mainThread());
    }
}
