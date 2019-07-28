package com.sololearn.android.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.viewmodel.repository.datasource.HomeDataFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeDataViewModel extends ViewModel {

    private Executor executor;
    private LiveData<String> networkState;
    private LiveData<PagedList<HomeDataResponseModel>> articleLiveData;

    public HomeDataViewModel() {
        init();
    }

    /*
     * Step 1: We are initializing an Executor class
     * Step 2: We are getting an instance of the DataSourceFactory class
     * Step 3: We are initializing the network state liveData as well.
     *         This will update the UI on the network changes that take place
     *         For instance, when the data is getting fetched, we would need
     *         to display a loader and when data fetching is completed, we
     *         should hide the loader.
     * Step 4: We need to configure the PagedList.Config.
     * Step 5: We are initializing the pageList using the config we created
     *         in Step 4 and the DatasourceFactory we created from Step 2
     *         and the executor we initialized from Step 1.
     */
    private void init() {
        executor = Executors.newFixedThreadPool(5);

        HomeDataFactory homeDataFactory = new HomeDataFactory();
        networkState = Transformations.switchMap(homeDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        articleLiveData = (new LivePagedListBuilder(homeDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }

    /*
     * Getter method for the network state
     */
    public LiveData<String> getNetworkState() {
        return networkState;
    }

    /*
     * Getter method for the pageList
     */
    public LiveData<PagedList<HomeDataResponseModel>> getArticleLiveData() {
        return articleLiveData;
    }
}