package com.sololearn.android.constants;

public interface AppConstants {
    String BASE_URL = "https://content.guardianapis.com/";
    String API_KEY = "api-key=212d23d3-c7b2-4273-8f1b-289a0803ca4b";

    // network calls
    String GET_HOME_DATA_URL = "search?" + AppConstants.API_KEY + "&show-fields=thumbnail&page=";

    // method type
    String GET = "GET";
}
