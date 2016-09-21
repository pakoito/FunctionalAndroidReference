package com.pacoworks.dereference;

import com.pacoworks.dereference.features.AppState;
import com.pacoworks.dereference.features.NavigationInteractorKt;
import com.pacoworks.dereference.features.Navigator;
import com.pacoworks.dereference.reactive.buddies.ActivityReactiveBuddy;

import rx.android.schedulers.AndroidSchedulers;

public class MainOrchestrator {

    public static void start(AppState state, Navigator navigator, ActivityReactiveBuddy activityReactiveBuddy) {
        NavigationInteractorKt.subscribeNavigation(state, navigator, activityReactiveBuddy, AndroidSchedulers.mainThread());
    }
}
