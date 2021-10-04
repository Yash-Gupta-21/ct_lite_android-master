package com.i9930.croptrails.Utility;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL_NODE = "https://api.croptrails.farm/";
    public static final String SELECTED_BASE_URL=BASE_URL_NODE;
    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(360, TimeUnit.SECONDS)
            .connectTimeout(360, TimeUnit.SECONDS)
            .build();

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static Retrofit getRetrofitInstance(final String mAuth) {
        Log.e("Retrofit", "Auth " + mAuth);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", mAuth)
                        .build();
                return chain.proceed(newRequest);
            }
        }).readTimeout(360, TimeUnit.SECONDS)
                .connectTimeout(360, TimeUnit.SECONDS)
                .writeTimeout(360, TimeUnit.SECONDS).build();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(SELECTED_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(SELECTED_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    /*public static Retrofit getRetrofitInstance(int flag) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }*/

}

//1. on back home data refresh( it should not happen)
//2. banner should be 16:9 ratio
//3. product page rating review  proper view
//4. wish list icon on product(hide circular background)
//5. order progress show dates also