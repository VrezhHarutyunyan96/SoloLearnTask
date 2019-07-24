package com.sololearn.android.listener;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Response;

public interface NetworkRequestListener<T> {
    void onResponse(Call<T> call, @NonNull Response<T> response);

    void onFailure(Call<T> call, @NonNull Throwable t);
}
