package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sololearn.android.R;
import com.sololearn.android.home.view.adapter.HomeRecyclerPagingAdapter;
import com.sololearn.android.home.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    // view
    private View view;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    // object
    private Context context;
    private HomeRecyclerPagingAdapter homeRecyclerPagingAdapter;
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
        initViews();
        initHomeDataAdapter();
        getHomeData();
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.homeDataRecyclerView);
    }

    private void initHomeDataAdapter() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        homeRecyclerPagingAdapter = new HomeRecyclerPagingAdapter(context.getApplicationContext());
        recyclerView.setAdapter(homeRecyclerPagingAdapter);
        homeDataViewModel.getArticleLiveData().observe(this, pagedList -> {
            homeRecyclerPagingAdapter.submitList(pagedList);
        });
        homeDataViewModel.getNetworkState().observe(this, networkState -> {
            homeRecyclerPagingAdapter.setNetworkState(networkState);
        });
    }

    private void getHomeData() {

    }

    private void initViewModel() {
        homeDataViewModel = ViewModelProviders.of(this).get(HomeDataViewModel.class);
    }
}
