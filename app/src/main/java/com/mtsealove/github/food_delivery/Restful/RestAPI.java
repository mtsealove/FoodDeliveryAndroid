package com.mtsealove.github.food_delivery.Restful;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtsealove.github.food_delivery.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

//RestApi
public class RestAPI {
    Retrofit retrofit;
    RetrofitService retrofitService;
    OkHttpClient okHttpClient;
    Context context;

    public RestAPI(Context context) {
        this.context = context;
        //통신 연결 클라이언트
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();
        //데이터를 받아올 API

        Gson gson= new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder=new Retrofit.Builder();

        retrofit = builder
                .baseUrl(context.getResources().getString(R.string.ip))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        retrofitService = retrofit.create(RetrofitService.class);
    }

    //바로 사용할 수 있는 인터페이스 반환
    public RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
