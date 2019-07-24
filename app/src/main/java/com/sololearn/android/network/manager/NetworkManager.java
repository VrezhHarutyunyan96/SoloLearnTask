package com.sololearn.android.network.manager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.listener.NetworkRequestListener;
import com.sololearn.android.network.webservice.ApiService;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkManager<BODY, RESPONSE> {

    public NetworkManager() {
    }

    private String url;
    private BODY BODY;

    public NetworkManager<BODY, RESPONSE> setUrl(String url) {
        this.url = url;
        return this;
    }

    public NetworkManager<BODY, RESPONSE> setBODY(BODY BODY) {
        this.BODY = BODY;
        return this;
    }

    public void initRequest(String methodType, NetworkRequestListener<RESPONSE> listener) {
        handleRequest(methodType, listener);
    }

    private void handleRequest(String methodType, final NetworkRequestListener<RESPONSE> listener) {
        RetrofitHandler retrofitHandler = new RetrofitHandler();
        switch (methodType) {
            case AppConstants.GET:
                Call<RESPONSE> call = (Call<RESPONSE>) retrofitHandler.getNetworkService().get(url.concat((String) BODY));
                call.enqueue(new Callback<RESPONSE>() {
                    @Override
                    public void onResponse(@NonNull Call<RESPONSE> call, @NonNull Response<RESPONSE> response) {
                        listener.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<RESPONSE> call, @NonNull Throwable t) {
                        listener.onFailure(call, t);
                    }
                });
                break;
        }

    }
}