package com.sololearn.android.home.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sololearn.android.R;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailItemFragment extends Fragment implements View.OnClickListener {

    //view
    private View view;
    private ImageView imageView;
    private ConstraintLayout back;

    // object
    private Context context;
    private String imageUrl;

    public DetailItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_item, container, false);
        context = getContext();
        getArgumentData(getArguments());
        initViews();
        loadImage(imageUrl);
        return view;
    }

    private void loadImage(String imageUrl) {
        if (imageUrl != null) {
            Glide
                    .with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    private void getArgumentData(Bundle arguments) {
        if (arguments != null) {
            imageUrl = arguments.getString(AppConstants.DETAIL_IMAGE);
        }
    }

    private void initViews() {
        imageView = view.findViewById(R.id.detailItemImageId);
        back = view.findViewById(R.id.backLayoutId);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLayoutId:
                handleBack();
                break;
        }
    }

    private void handleBack() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentByTag(HomeFragment.TAG);
            if (fragment == null)
                fragment = new HomeFragment();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.createFragment(R.id.fragmentContainer, fragment);
            }
        }
    }
}
