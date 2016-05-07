/*
 * Copyright (C) 2015 Federico Paolinelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.whiterabbit.droidodoro.trelloclient;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiterabbit.droidodoro.constants.Keys;
import com.whiterabbit.droidodoro.model.Board;
import com.whiterabbit.droidodoro.model.TrelloList;
import com.whiterabbit.droidodoro.storage.PreferencesUtils;


import java.util.List;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class TrelloClient {
    @Inject
    PreferencesUtils mPreferences;

    private TrelloService mClient;

    public static Gson getGSon() {
        GsonBuilder builder = new GsonBuilder();

        return builder.create();
    }

    public TrelloClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(chain -> {
            Request original = chain.request();
            Log.d("FEDE", original.url().toString());
            HttpUrl newUrl = original.url().newBuilder()
                                  .addQueryParameter("key", Keys.TRELLO_KEY)
                                  .addQueryParameter("token", mPreferences.getAuthToken()).build();

            Request enhancedRequest = original.newBuilder()
                                            .url(newUrl).build();
            Log.d("FEDE", enhancedRequest.url().toString());
            return chain.proceed(enhancedRequest);
        });

        mClient = new Retrofit.Builder()
                              .baseUrl("http://api.openweathermap.org/data/2.5/")
                              .client(client)
                              .addConverterFactory(GsonConverterFactory.create(getGSon()))
                              .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                              .build()
                              .create(TrelloService.class);
    }

    public Observable<List<Board>> getBoards() {
        return mClient.listMyBoards();
    }

    public Observable<List<TrelloList>> getLists(String boardId) {
        return mClient.getLists(boardId);
    }
}
