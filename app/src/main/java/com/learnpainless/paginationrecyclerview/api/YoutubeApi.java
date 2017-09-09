package com.learnpainless.paginationrecyclerview.api;

import com.learnpainless.paginationrecyclerview.models.YoutubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pawneshwer on 9/9/17.
 * Retrofit API class to call APIs.
 */

public interface YoutubeApi {
    @GET("search")
    Call<YoutubeResponse> searchVideo(@Query("q") String query,
                                  @Query("type") String type,
                                  @Query("key") String key,
                                  @Query("part") String part,
                                  @Query("maxResults") String maxResults,
                                  @Query("pageToken") String pageToken);
}
