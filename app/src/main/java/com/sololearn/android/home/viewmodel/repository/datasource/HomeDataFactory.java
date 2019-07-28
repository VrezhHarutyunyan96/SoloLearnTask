package com.sololearn.android.home.viewmodel.repository.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class HomeDataFactory extends DataSource.Factory {

    private MutableLiveData<HomeDataSource> mutableLiveData;
    private HomeDataSource homeDataSource;

    public HomeDataFactory() {
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        homeDataSource = new HomeDataSource();
        mutableLiveData.postValue(homeDataSource);
        return homeDataSource;
    }
  
    public MutableLiveData<HomeDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}