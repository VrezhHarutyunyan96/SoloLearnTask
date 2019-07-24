package com.sololearn.android.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.viewmodel.repository.HomeDataRepository;

public class HomeDataViewModel extends AndroidViewModel {
    private LiveData<HomeDataResponseModel> liveData;
    private HomeDataRepository repository;

    public HomeDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void getHomeData(int page) {
       if (repository == null)
           repository = new HomeDataRepository();
       liveData = repository.initRequest(String.valueOf(page));
    }

    public LiveData<HomeDataResponseModel> getLiveData() {
        return liveData;
    }
}
