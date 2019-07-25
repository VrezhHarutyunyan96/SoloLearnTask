package com.sololearn.android.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.viewmodel.datasource.ItemDataSource;
import com.sololearn.android.home.viewmodel.datasource.ItemDataSourceFactory;

public class HomeDataViewModel extends AndroidViewModel {
    public LiveData itemPagedList;
    public LiveData<PageKeyedDataSource<Integer, HomeDataResponseModel>> liveDataSource;

    public HomeDataViewModel(@NonNull Application application) {
        super(application);
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ItemDataSource.PAGE_SIZE).build();
        itemPagedList = (new LivePagedListBuilder<Integer, HomeDataResponseModel>(itemDataSourceFactory, pagedListConfig)).build();
    }

    public LiveData getItemPagedList() {
        return itemPagedList;
    }
}
