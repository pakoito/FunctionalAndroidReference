package com.pacoworks.dereference.features.home;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.core.reactive.None;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

public class HomeScreen extends BaseController implements HomeView {

    private final PublishRelay<None> mClicksPRelay = PublishRelay.create();

    public HomeScreen() {
        super();
        HomeInteractorKt.startHomeInteractor(this, new HomeState());
    }

    @NonNull
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        final TextView textView = new TextView(container.getContext());
        textView.setText("slsgsagasfgasfgk\nfjghlkasjfhgkljashgaakljhskljghsk\nljghksjhg");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClicksPRelay.call(None.VOID);
            }
        });
        return textView;
    }

    @Override
    public void setTitle(@NonNull String title) {
        getActivity().setTitle(title);
    }

    @NotNull
    @Override
    public Observable<None> clicks() {
        return mClicksPRelay.asObservable();
    }
}
