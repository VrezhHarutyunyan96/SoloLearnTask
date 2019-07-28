package com.sololearn.android.home.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sololearn.android.R;
import com.sololearn.android.home.view.adapter.HomePagerAdapter;
import com.sololearn.android.home.view.fragment.HomeFragment;
import com.sololearn.android.home.view.fragment.SavedFragment;
import com.sololearn.android.service.CheckNewDataService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SavedFragment());
        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
    }

    public void createFragment(int resId, Fragment fragment) {
        if (getSupportFragmentManager() != null) {
            // get fragment from container
            Fragment createdFragment = getSupportFragmentManager()
                    .findFragmentByTag(fragment.getClass().getName());
            if (createdFragment == null) {
                fragmentTransaction(resId, fragment);
            } else {
                fragmentTransaction(resId, fragment);
            }
        }
    }

    private void fragmentTransaction(int resId, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, fragment, fragment.getClass().getName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        handleBackPressed(getSupportFragmentManager(), R.id.fragmentContainer);
        super.onBackPressed();
    }

    private void handleBackPressed(FragmentManager supportFragmentManager, int resId) {
        if (supportFragmentManager != null) {
            Fragment fragment = supportFragmentManager.findFragmentById(resId);
            if (fragment != null) {
                if (fragment instanceof HomeFragment) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, CheckNewDataService.class));
    }
}
