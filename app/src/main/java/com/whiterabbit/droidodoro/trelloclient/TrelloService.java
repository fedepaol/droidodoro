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


import com.whiterabbit.droidodoro.model.Board;
import com.whiterabbit.droidodoro.model.Card;
import com.whiterabbit.droidodoro.model.TrelloList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface TrelloService {
    @GET("1/members/me/boards?fields=name,id")
    Observable<List<Board>> listMyBoards();

    @GET("1/boards/{boardid}/lists?fields=name,id")
    Observable<List<TrelloList>> getLists(@Path("boardid") String boardid);

    @GET("1/lists/{listid}/cards?card_fields=name")
    Observable<List<Card>> getCards(@Path("listid") String listid);

    @PUT("1/cards/{cardid}")
    Call<ResponseBody> updateCard(@Path("cardid") String cardid, @Query("idList") String listId);

    @POST("1/cards/{cardid}/actions/comments/")
    Call<ResponseBody> addComment(@Path("cardid") String cardid, @Query("text") String comment);

}
