package com.sololearn.android.home.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sololearn.android.home.database.entity.SavedDataModel;

import java.util.List;

@Dao
public interface SavedDao {

    @Insert
    void save(SavedDataModel data);

    @Query("SELECT * FROM saved_data")
    LiveData<List<SavedDataModel>> getAll();

}
