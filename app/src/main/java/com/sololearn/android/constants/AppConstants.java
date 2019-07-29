package com.sololearn.android.constants;

public interface AppConstants {
    String BASE_URL = "https://content.guardianapis.com/";
    String API_KEY = "api-key=212d23d3-c7b2-4273-8f1b-289a0803ca4b";

    // network calls
    String GET_HOME_DATA_URL = "search?" + AppConstants.API_KEY + "&show-fields=thumbnail&page=";

    // method type
    String GET = "GET";

    // key
    String DETAIL_IMAGE = "detail_image";
    String TOTAL_DATA = "total_data";
    String NEW_ITEM = "total_data";
    String NEW_ITEM_IMAGE = "total_data_image";
    String NEW_ITEM_TITLE = "total_data_title";

    // room
    int ROOM_VERSION = 1;
    String DB_NAME_SAVED_DATA = "savedData.db";
}
