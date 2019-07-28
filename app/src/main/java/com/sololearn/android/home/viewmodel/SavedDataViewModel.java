package com.sololearn.android.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sololearn.android.home.database.dao.SavedDao;
import com.sololearn.android.home.database.entity.SavedDataModel;
import com.sololearn.android.home.database.room.SavedRoom;
import com.sololearn.android.home.viewmodel.repository.SavedDataRepository;

import java.util.List;

public class SavedDataViewModel extends AndroidViewModel {
    private SavedDao savedDao;
    private LiveData<List<SavedDataModel>> liveData;
    private SavedDataRepository savedDataRepository;


    public SavedDataViewModel(@NonNull Application application) {
        super(application);
        SavedRoom savedRoom = SavedRoom.getINSTANCE();
        savedDao = savedRoom.save();
        if (liveData == null)
            liveData = savedDao.getAll();
    }

    public void save(String sectionName, String title, String imageUrl) {
        savedDataRepository = new SavedDataRepository(savedDao);
        savedDataRepository.save(new SavedDataModel(sectionName, title, imageUrl));
    }

    public LiveData<List<SavedDataModel>> getLiveData() {
        return liveData;
    }
}
