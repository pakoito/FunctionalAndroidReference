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

package com.pacoworks.dereference.features.cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxrelay.PublishRelay;
import com.pacoworks.dereference.R;
import com.pacoworks.dereference.features.cache.model.AgotCharacterKt;
import com.pacoworks.dereference.features.cache.model.IncorrectInfo;
import com.pacoworks.dereference.features.cache.model.KnownAgotCharacter;
import com.pacoworks.dereference.features.cache.model.NetworkError;
import com.pacoworks.dereference.features.cache.model.Unavailable;
import com.pacoworks.dereference.features.cache.model.UnknownAgotCharacter;
import com.pacoworks.dereference.features.cache.services.CacheExampleAgotServiceKt;
import com.pacoworks.dereference.features.global.BaseController;
import com.pacoworks.dereference.features.global.DereferenceApplication;
import com.pacoworks.rxsealedunions.Union2;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Controller implementing CacheExampleView
 */
public class CacheScreen extends BaseController implements CacheExampleView {

    private final CacheExampleState cacheExampleState;

    private final PublishRelay<String> filterSelectionPRelay = PublishRelay.create();

    private TextView text;

    private Spinner spin;

    public CacheScreen() {
        super();
        cacheExampleState = new CacheExampleState();
        CacheExampleInteractorKt.subscribeCacheExampleInteractor(this, cacheExampleState, new Function1<String, Observable<Union2<UnknownAgotCharacter, KnownAgotCharacter>>>() {
            @Override
            public Observable<Union2<UnknownAgotCharacter, KnownAgotCharacter>> invoke(String charId) {
                /* FIXME getActivity() throws an NPE if called before the controller is attached */
                try {
                    final Activity activity = getActivity();
                    return CacheExampleAgotServiceKt.characterInfo(
                            charId,
                            DereferenceApplication.get(activity).getInjector().getAgotApi(),
                            Schedulers.newThread());
                } catch (Exception ignore) {
                    return Observable.just(AgotCharacterKt.createUnknownUnavailableCharacter(charId));
                }
            }
        });
    }

    @NonNull
    @Override
    protected View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        final View inflate = inflater.inflate(R.layout.screen_cache, container, false);
        spin = (Spinner) inflate.findViewById(R.id.screen_cached_spinner);
        spin.setAdapter(new ArrayAdapter<String>(context, R.layout.widget_text));
        text = (TextView) inflate.findViewById(R.id.screen_cached_text);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getAdapter().getItem(position);
                filterSelectionPRelay.call(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return inflate;
    }

    @Override
    protected void attachBinders() {
        CacheExampleInteractorKt.bindCacheExample(this, cacheExampleState);
    }

    @Override
    public void showCharacterInfo(@NotNull Union2<UnknownAgotCharacter, KnownAgotCharacter> info) {
        info.continued(new Action1<UnknownAgotCharacter>() {
            @Override
            public void call(UnknownAgotCharacter unknownAgotCharacter) {
                text.setText("[" + unknownAgotCharacter.getId() + "] not found because " +
                        unknownAgotCharacter.getReason()
                                .join(new Func1<Unavailable, String>() {
                                    @Override
                                    public String call(Unavailable unavailable) {
                                        return "it is being fetched";
                                    }
                                }, new Func1<NetworkError, String>() {
                                    @Override
                                    public String call(NetworkError networkError) {
                                        return "there was a network problem";
                                    }
                                }, new Func1<IncorrectInfo, String>() {
                                    @Override
                                    public String call(IncorrectInfo incorrectInfo) {
                                        return "the character response was malformed";
                                    }
                                }));
                text.setTextColor(unknownAgotCharacter.getReason().join(new Func1<Unavailable, Integer>() {
                    @Override
                    public Integer call(Unavailable unavailable) {
                        return Color.YELLOW;
                    }
                }, new Func1<NetworkError, Integer>() {
                    @Override
                    public Integer call(NetworkError networkError) {
                        return Color.RED;
                    }
                }, new Func1<IncorrectInfo, Integer>() {
                    @Override
                    public Integer call(IncorrectInfo incorrectInfo) {
                        return Color.RED;
                    }
                }));


            }
        }, new Action1<KnownAgotCharacter>() {
            @Override
            public void call(KnownAgotCharacter knownAgotCharacter) {
                text.setTextColor(Color.WHITE);
                text.setText(String.format(Locale.US, "This is%n%n%s %s",
                        knownAgotCharacter.getTitles().isEmpty() ? "" : knownAgotCharacter.getTitles().get(0),
                        knownAgotCharacter.getName()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void filterList(@NotNull List<String> ids) {
        final ArrayAdapter<String> adapter = (ArrayAdapter<String>) spin.getAdapter();
        adapter.addAll(ids);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void currentFilter(@NotNull String id) {
        final ArrayAdapter<String> adapter = (ArrayAdapter<String>) spin.getAdapter();
        spin.setSelection(adapter.getPosition(id));
    }

    @NotNull
    @Override
    public Observable<String> filterSelected() {
        return filterSelectionPRelay.asObservable();
    }
}
