package com.pacoworks.dereference.features.home;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pacoworks.dereference.BaseController;

public class HomeScreen extends BaseController implements HomeView {

    public HomeScreen() {
        super();
        HomeInteractor.start(this);
    }

    @NonNull
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return new TextView(container.getContext());
    }

    @Override
    public void setTitle(@NonNull String title) {
        getActivity().setTitle(title);
    }
}
