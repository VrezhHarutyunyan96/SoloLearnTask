package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sololearn.android.R;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.view.adapter.HomeRecyclerPagingAdapter;
import com.sololearn.android.home.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    // view
    private View view;
    private RecyclerView recyclerView;
    private HomeRecyclerPagingAdapter homeRecyclerPagingAdapter;
    RecyclerView.LayoutManager layoutManager;
    // object
    private Context context;
    private HomeDataViewModel homeDataViewModel;
    private Handler handler;
    // variable
    private int page = 0;
    private boolean loading;

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
        handler = new Handler();
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.homeDataRecyclerView);
    }

    private void initHomeDataAdapter() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        homeRecyclerPagingAdapter = new HomeRecyclerPagingAdapter(context);
        recyclerView.setAdapter(homeRecyclerPagingAdapter);
    }

    private void getHomeData() {
        homeDataViewModel.itemPagedList.observe(this, new Observer<PagedList<HomeDataResponseModel>>() {
            @Override
            public void onChanged(@Nullable final PagedList<HomeDataResponseModel> items) {
                homeRecyclerPagingAdapter.submitList(items);
            }
        });
    }

    private void initViewModel() {
        homeDataViewModel = ViewModelProviders.of(this).get(HomeDataViewModel.class);
    }
}
