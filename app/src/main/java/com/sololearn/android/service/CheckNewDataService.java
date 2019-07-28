package com.sololearn.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.sololearn.android.AppApplication;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.helper.SharedHelper;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.network.listener.NetworkRequestListener;
import com.sololearn.android.network.manager.NetworkManager;
import com.sololearn.android.notification.SoloLearnNotification;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

public class CheckNewDataService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer(true);
        return START_STICKY;
    }

    private void startTimer(boolean isStart) {
        if (isStart) {
            // schedule timer corresponding delay
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    initCheckRequest();
                    count++;
                }
            };
            timer.schedule(timerTask, 30000);
        } else {
            // stop timer
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        }
    }

    private void initCheckRequest() {
        // init request
        NetworkManager<String, HomeDataResponseModel> networkManager = new NetworkManager<>();
        networkManager.setBODY(String.valueOf(1));
        networkManager.setUrl(AppConstants.GET_HOME_DATA_URL);
        networkManager.initRequest(AppConstants.GET, new NetworkRequestListener<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, @NonNull Response<HomeDataResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    HomeDataResponseModel data = gson.fromJson(json, HomeDataResponseModel.class);
                    boolean isHaveNewItem = handleISHaveNewItem(data);
                    handleUpdateData(isHaveNewItem);
                    startTimer(true);
                    String imageUrl = data.getResponse().getResults().get(0).getFields().getThumbnail();
                    if (imageUrl != null && !imageUrl.isEmpty())
                        SharedHelper.putKey(null, AppConstants.NEW_ITEM_IMAGE, imageUrl);
                } else {
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, @NonNull Throwable t) {
                startTimer(true);
            }
        });
    }

    private void handleUpdateData(boolean isHaveNewItem) {
        if (isHaveNewItem == true || count > 0) {
            if (!AppApplication.isBackground) {
                LocalBroadcastManager
                        .getInstance(CheckNewDataService.this)
                        .sendBroadcast(new Intent(AppConstants.NEW_ITEM));
            } else {
                // show notification
                SoloLearnNotification soloLearnNotification = new SoloLearnNotification(CheckNewDataService.this);
                soloLearnNotification.show("aa", "");
            }
            count = 0;
        }
    }

    private boolean handleISHaveNewItem(HomeDataResponseModel data) {
        String totalValue = SharedHelper.getKey(null, AppConstants.TOTAL_DATA);
        if (totalValue != null && !totalValue.isEmpty()) {
            int total = Integer.parseInt(totalValue);
            int updatedTotalValue = data.getResponse().getTotal();
            return total != updatedTotalValue;
        }
        saveTotalValue(String.valueOf(data.getResponse().getTotal()));
        return false;
    }

    private void saveTotalValue(String value) {
        if (value != null)
            SharedHelper.putKey(null, AppConstants.TOTAL_DATA, value);
    }

}
