package com.sololearn.android.home.viewmodel.repository.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.listener.NetworkRequestListener;
import com.sololearn.android.network.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class FeedDataSource extends PageKeyedDataSource<Long, HomeDataResponseModel> {
    /*
     * Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public FeedDataSource() {
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }


    /*
     * This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Long> params,
                            @NonNull final LoadInitialCallback<Long, HomeDataResponseModel> callback) {

        initialLoading.postValue("LOADING");
        networkState.postValue("LOADING");

        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(1));
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

                    callback.onResult(list, null, 2l);
                    initialLoading.postValue("LOADED");
                    networkState.postValue("LOADED");
                } else {
                    networkState.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {
                networkState.postValue(null);
            }
        });
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, HomeDataResponseModel> callback) {

    }

    /*
     * This method it is responsible for the subsequent call to load the data page wise.
     * This method is executed in the background thread
     * We are fetching the next page data from the api
     * and passing it via the callback method to the UI.
     * The "params.key" variable will have the updated value.
     */
    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params,
                          @NonNull final LoadCallback<Long, HomeDataResponseModel> callback) {


        networkState.postValue("LOADING");
        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(params.key));
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

                    long nextKey = (Objects.equals(params.key, list.get(0).getResponse().getTotal())) ? 0 : params.key + 1;
                    callback.onResult(list, nextKey);
                    networkState.postValue("LOADED");
                } else {
                    networkState.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}