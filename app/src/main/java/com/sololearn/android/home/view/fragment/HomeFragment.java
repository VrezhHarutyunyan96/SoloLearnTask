package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sololearn.android.R;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.view.adapter.HomeRecyclerAdapter;
import com.sololearn.android.home.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    // view
    private View view;
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager = null;
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
        getHomeData();
        handler = new Handler();
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.homeDataRecyclerView);
    }

    private void initHomeDataAdapter(HomeDataResponseModel data) {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        homeRecyclerAdapter = new HomeRecyclerAdapter(context, data);
        recyclerView.setAdapter(homeRecyclerAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void getHomeData() {
        homeDataViewModel.getHomeData(1);
        final Observer<HomeDataResponseModel> observer = new Observer<HomeDataResponseModel>() {
            @Override
            public void onChanged(HomeDataResponseModel homeDataResponseModel) {
                initHomeDataAdapter(homeDataResponseModel);
            }
        };
        homeDataViewModel.getLiveData().observe(this, observer);
    }

    private void initViewModel() {
        homeDataViewModel = ViewModelProviders.of(this).get(HomeDataViewModel.class);
    }

    void checkEndOffset() {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();

        int firstVisibleItemPosition;
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            // https://code.google.com/p/android/issues/detail?id=181461
            if (recyclerView.getLayoutManager().getChildCount() > 0) {
                firstVisibleItemPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
            } else {
                firstVisibleItemPosition = 0;
            }
        } else {
            throw new IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager");
        }
        System.out.println("djnbdjnjdnjdn====>>>>>>  " + visibleItemCount);
        System.out.println("djnbdjnjdnjdn====>>>>>>  " + firstVisibleItemPosition);
        if (visibleItemCount + firstVisibleItemPosition == 10) {
            System.out.println();
        }
    }

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            checkEndOffset(); // Each time when list is scrolled check if end of the list is reached
        }
    };
}
