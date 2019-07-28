package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sololearn.android.R;
import com.sololearn.android.home.database.entity.SavedDataModel;
import com.sololearn.android.home.view.adapter.SavedRecyclerViewAdapter;
import com.sololearn.android.home.viewmodel.SavedDataViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {
    // view
    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private SavedRecyclerViewAdapter savedRecyclerViewAdapter;
    // object
    private Context context;
    private SavedDataViewModel savedDataViewModel;

    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_saved, container, false);
        context = getContext();
        initViewModel();
        initViews();
        getSavedData();
        return view;
    }

    private void initViewModel() {
        savedDataViewModel = ViewModelProviders.of(this).get(SavedDataViewModel.class);
    }

    private void getSavedData() {
        final Observer<List<SavedDataModel>> observer = savedDataModels -> {
            if (savedDataModels != null) {
                initSavedAdapter(savedDataModels);
            }
        };
        savedDataViewModel.getLiveData().observe(this, observer);
    }

    private void initSavedAdapter(List<SavedDataModel> savedDataModels) {
        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        savedRecyclerViewAdapter = new SavedRecyclerViewAdapter(context, savedDataModels);
        recyclerView.setAdapter(savedRecyclerViewAdapter);
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.savedRcId);
    }


}
