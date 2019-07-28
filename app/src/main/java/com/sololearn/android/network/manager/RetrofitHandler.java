package com.sololearn.android.network.manager;

import android.content.Context;

import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.network.webservice.ApiService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {
    private Context context;

    public RetrofitHandler(Context context) {
        this.context = context;
    }

    public ApiService getNetworkService() {
        return initRetrofit(AppConstants.BASE_URL).create(ApiService.class);
    }

    private Retrofit initRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(addCache())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient addCache() {
        int cacheSize = 20 * 1024 * 1024; // 20 MB

        // Create Cache
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http"), cacheSize);
        } catch (Exception e) {
            e.getMessage();
        }

        // Create OkHttpClient and add Time Out
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);


        // Add Cache-Control Interceptor
        okHttpClient.networkInterceptors().add(mCacheControlInterceptor);
        return okHttpClient.build();

    }

    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                request.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
            }

            Response response = chain.proceed(request);

            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=86400")
                    .build();
        }
    };
}
