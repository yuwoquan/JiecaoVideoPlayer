package com.example.jiecaovideoplayer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 鱼握拳 on 2017/12/19.
 */

public class RetrofitManger {
    private static final long cacheSize = 1024 * 1024 * 20;// 缓存文件最大限制大小20M
    private static final String TAG = "RetrofitManger";
    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/okttpcaches"; // 设置缓存文件路径
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);
    private static final String DoubanBook_Url = "http://baobab.kaiyanapp.com/api/v4/tabs/";
    private static Retrofit retrofit;
    public static Retrofit getRetrofit(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                 Log.d(TAG, "请求网页为："+message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)//连接超时时间
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时时间
                .readTimeout(10,TimeUnit.SECONDS)//读取超时时间
                .retryOnConnectionFailure(true)
                .cache(cache)//缓存
                .build();
        if (retrofit == null){
            synchronized (RetrofitManger.class){
                if (retrofit == null){
                    retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(DoubanBook_Url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}

