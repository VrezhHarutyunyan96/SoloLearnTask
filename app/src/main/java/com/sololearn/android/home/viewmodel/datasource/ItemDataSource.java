package com.sololearn.android.home.viewmodel.datasource;


import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.listener.NetworkRequestListener;
import com.sololearn.android.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ItemDataSource extends PageKeyedDataSource<Integer, HomeDataResponseModel> {

    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private final String API_KEY = "8439361-5e1e53a0e1b58baa26ab398f7";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, HomeDataResponseModel> callback) {

        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(FIRST_PAGE));
        networkManager.setUrl(AppConstants.GET_HOME_DATA_URL);
        networkManager.initRequest(AppConstants.GET, new NetworkRequestListener<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, @NonNull Response<HomeDataResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    HomeDataResponseModel data = gson.fromJson(json, HomeDataResponseModel.class);
                    List<HomeDataResponseModel> list = new ArrayList<>();
                    list.add(data);
                    callback.onResult(list, null, FIRST_PAGE + 1);
                } else {

                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {

            }
        });

   /*     RetrofitClient.getInstance()
                .getApi().getAllImages(API_KEY, FIRST_PAGE, "3")
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().getImages(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });*/
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, HomeDataResponseModel> callback) {

        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(FIRST_PAGE));
        networkManager.setUrl(AppConstants.GET_HOME_DATA_URL);
        networkManager.initRequest(AppConstants.GET, new NetworkRequestListener<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, @NonNull Response<HomeDataResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    HomeDataResponseModel data = gson.fromJson(json, HomeDataResponseModel.class);
                    List<HomeDataResponseModel> list = new ArrayList<>();
                    list.add(data);
                    Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(list, adjacentKey);
                } else {

                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {

            }
        });
        /*     RetrofitClient.getInstance()
                .getApi().getAllImages(API_KEY, params.key, "3")
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body().getImages(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });*/
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, HomeDataResponseModel> callback) {

        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(FIRST_PAGE));
        networkManager.setUrl(AppConstants.GET_HOME_DATA_URL);
        networkManager.initRequest(AppConstants.GET, new NetworkRequestListener<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, @NonNull Response<HomeDataResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    HomeDataResponseModel data = gson.fromJson(json, HomeDataResponseModel.class);
                    List<HomeDataResponseModel> list = new ArrayList<>();
                    list.add(data);
                    callback.onResult(list, params.key + 1);
                } else {

                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {

            }
        });
     /*   RetrofitClient.getInstance()
                .getApi()
                .getAllImages(API_KEY, params.key, "3")
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        if (response.body() != null && true) {
                            callback.onResult(response.body().getImages(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });*/
    }
}