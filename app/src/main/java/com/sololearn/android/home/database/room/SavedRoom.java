package com.sololearn.android.home.database.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sololearn.android.AppApplication;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.database.dao.SavedDao;
import com.sololearn.android.home.database.entity.SavedDataModel;

@Database(entities = SavedDataModel.class, version = AppConstants.ROOM_VERSION, exportSchema = false)
public abstract class SavedRoom extends RoomDatabase {
    private static SavedRoom INSTANCE;

    public static SavedRoom getINSTANCE() {
        if (INSTANCE == null) {
            return INSTANCE = Room
                    .databaseBuilder(AppApplication.appApplication, SavedRoom.class, AppConstants.DB_NAME_SAVED_DATA)
                    .allowMainThreadQueries()
                    .build();
        } else {
            return INSTANCE;
        }
    }

    public abstract SavedDao save();

}