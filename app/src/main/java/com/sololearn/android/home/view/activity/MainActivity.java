package com.sololearn.android.home.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.sololearn.android.R;
import com.sololearn.android.home.view.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // show home fragment
        createFragment(R.id.fragmentContainer, new HomeFragment());
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
}