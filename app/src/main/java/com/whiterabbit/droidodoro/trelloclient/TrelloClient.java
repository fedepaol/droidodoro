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



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiterabbit.droidodoro.BuildConfig;
import com.whiterabbit.droidodoro.storage.KeyValueStorage;
import com.whiterabbit.droidodoro.trelloclient.model.Board;
import com.whiterabbit.droidodoro.trelloclient.model.Card;
import com.whiterabbit.droidodoro.trelloclient.model.TrelloList;


import java.io.IOException;
import java.util.List;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class TrelloClient {
    KeyValueStorage mKeyValueStorage;

    private TrelloService mClient;

    public static Gson getGSon() {
        GsonBuilder builder = new GsonBuilder();

        return builder.create();
    }

    public TrelloClient(KeyValueStorage preferences) {
        mKeyValueStorage = preferences;
        OkHttpClient okClient = new OkHttpClient.Builder()
                                .addInterceptor(chain -> {
                                    Request original = chain.request();
                                    HttpUrl newUrl = original.url().newBuilder()
                                            .addQueryParameter("key", BuildConfig.TRELLO_API_KEY)
                                            .addQueryParameter("token", mKeyValueStorage.getAuthToken()).build();

                                    Request enhancedRequest = original.newBuilder()
                                            .url(newUrl).build();
                                    return chain.proceed(enhancedRequest);
                                }).build();


        mClient = new Retrofit.Builder()
                              .baseUrl("https://api.trello.com/")
                              .client(okClient)
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

    public Observable<List<Card>> getCards(String listId) {
        return mClient.getCards(listId);
    }

    public boolean updateCard(String card, String newList) throws IOException {
        Call<ResponseBody> res = mClient.updateCard(card, newList);
        Response<ResponseBody> result = res.execute();
        return result.isSuccessful();
    }

    public boolean setCommentToCard(String card, String comment) throws IOException {
        Call<ResponseBody> res = mClient.addComment(card, comment);
        Response<ResponseBody> result = res.execute();
        return result.isSuccessful();
    }
}
