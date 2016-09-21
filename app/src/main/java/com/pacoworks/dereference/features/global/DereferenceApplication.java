package com.pacoworks.dereference.features.global;

import android.app.Application;
import android.content.Context;

public class DereferenceApplication extends Application {

    Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();
        injector = new Injector(this);
    }

    public static DereferenceApplication get(Context context){
        return (DereferenceApplication) context.getApplicationContext();
    }

    public Injector getInjector() {
        return injector;
    }
}
