package com.sololearn.android.home.viewmodel.repository;

import android.os.AsyncTask;

import com.sololearn.android.home.database.dao.SavedDao;
import com.sololearn.android.home.database.entity.SavedDataModel;

public class SavedDataRepository {
    private SavedDao savedDao;

    public SavedDataRepository(SavedDao savedDao) {
        this.savedDao = savedDao;
    }

    public void save(SavedDataModel data) {
        // saved pined Item
        new SavedRoomAsync().execute(data);
    }


    private class SavedRoomAsync extends AsyncTask<SavedDataModel, Void, Void> {
        @Override
        protected Void doInBackground(SavedDataModel... data) {
            savedDao.save(data[0]);
            return null;
        }
    }


}
