package com.learnpainless.paginationrecyclerview.api;

import com.learnpainless.paginationrecyclerview.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pawneshwer on 9/9/17.
 * Retrofit's base service builder class.
 */

public class APIService {
    private static <LPL> LPL createService(Class<LPL> cls) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.BASE_URL).build();
        return retrofit.create(cls);
    }

    public static final YoutubeApi youtubeApi = createService(YoutubeApi.class);
}
