package com.midsummer.mynews.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midsummer.mynews.helper.Constant;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by nienb on 10/2/16.
 */
public class APIService {
    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.APIENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static APIEndpoint build(){
        return retrofit.create(APIEndpoint.class);
    }
}
