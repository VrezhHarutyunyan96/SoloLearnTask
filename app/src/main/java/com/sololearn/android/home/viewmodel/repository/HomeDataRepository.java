package com.sololearn.android.home.viewmodel.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.listener.NetworkRequestListener;
import com.sololearn.android.network.manager.NetworkManager;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Response;

public class HomeDataRepository {
    public LiveData<HomeDataResponseModel> initRequest(String page) {
        final MutableLiveData<HomeDataResponseModel> mutableLiveData = new MutableLiveData<>();
        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(page);
        networkManager.setUrl(AppConstants.GET_HOME_DATA_URL);
        networkManager.initRequest(AppConstants.GET, new NetworkRequestListener<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, @NonNull Response<HomeDataResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    HomeDataResponseModel data = gson.fromJson(json, HomeDataResponseModel.class);
                    mutableLiveData.setValue(data);
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
