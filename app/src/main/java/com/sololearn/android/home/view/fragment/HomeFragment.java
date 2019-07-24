package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sololearn.android.R;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    // view
    private View view;
    // object
    private Context context;
    private HomeDataViewModel homeDataViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        initViewModel();
        getHomeData();
        return view;
    }

    private void getHomeData() {
        homeDataViewModel.getHomeData(1);
        final Observer<HomeDataResponseModel> observer = new Observer<HomeDataResponseModel>() {
            @Override
            public void onChanged(HomeDataResponseModel homeDataResponseModel) {
                System.out.println();
            }
        };
        homeDataViewModel.getLiveData().observe(this, observer);
    }

    private void initViewModel() {
        homeDataViewModel = ViewModelProviders.of(this).get(HomeDataViewModel.class);
    }

}
