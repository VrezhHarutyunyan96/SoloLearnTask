package com.sololearn.android.network.manager;

import androidx.annotation.NonNull;

import com.sololearn.android.AppApplication;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.network.listener.NetworkRequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unchecked")
public class NetworkManager<BODY, RESPONSE> {

    public NetworkManager() {
    }

    private String url;
    private BODY BODY;
    private BODY pageSize;

    public NetworkManager<BODY, RESPONSE> setUrl(String url) {
        this.url = url;
        return this;
    }

    public NetworkManager<BODY, RESPONSE> setPage(BODY BODY) {
        this.BODY = BODY;
        return this;
    }

    public NetworkManager<BODY, RESPONSE> setPageSize(BODY BODY) {
        this.pageSize = BODY;
        return this;
    }

    public void initRequest(String methodType, NetworkRequestListener<RESPONSE> listener) {
        handleRequest(methodType, listener);
    }


    private void handleRequest(String methodType, final NetworkRequestListener<RESPONSE> listener) {
        RetrofitHandler retrofitHandler = new RetrofitHandler(AppApplication.appApplication);
        switch (methodType) {
            case AppConstants.GET:
                Call<RESPONSE> call = (Call<RESPONSE>) retrofitHandler.getNetworkService().get(url.concat((String) BODY).concat("&page-size=" + pageSize));
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
