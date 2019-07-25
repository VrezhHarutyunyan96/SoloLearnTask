package com.sololearn.android.home.viewmodel.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.sololearn.android.home.model.HomeDataResponseModel;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, HomeDataResponseModel>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource<Integer, HomeDataResponseModel> create() {
        ItemDataSource itemDataSource = new ItemDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, HomeDataResponseModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}