package com.pacoworks.dereference;

import com.pacoworks.dereference.core.reactive.buddies.ActivityReactiveBuddy;
import com.pacoworks.dereference.core.ui.Navigator;
import com.pacoworks.dereference.features.global.AppState;
import com.pacoworks.dereference.features.global.NavigationInteractorKt;

import rx.android.schedulers.AndroidSchedulers;

public class MainOrchestrator {

    public static void start(AppState state, Navigator navigator, ActivityReactiveBuddy activityReactiveBuddy) {
        NavigationInteractorKt.subscribeNavigation(state, navigator, activityReactiveBuddy, AndroidSchedulers.mainThread());
    }
}
