package com.sjtubus.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjtubus.utils.GsonSqlDateTypeAdapter;
import com.sjtubus.utils.GsonUtilDateTypeAdapter;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://106.14.181.49:8080/";

    private static BusApi busApi;

    public static BusApi getBusApi(){
        if(busApi == null){
            Executor executor = Executors.newCachedThreadPool();
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class,new GsonUtilDateTypeAdapter())
                    .registerTypeAdapter(java.sql.Date.class,new GsonSqlDateTypeAdapter())
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .callbackExecutor(executor)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(BusOkHttpClient.getInstance())
                    .build();

            busApi = retrofit.create(BusApi.class);
        }
        return busApi;
    }

}
