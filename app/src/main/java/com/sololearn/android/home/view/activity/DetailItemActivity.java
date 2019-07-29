package com.sololearn.android.home.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.sololearn.android.R;
import com.sololearn.android.constants.AppConstants;

public class DetailItemActivity extends AppCompatActivity implements View.OnClickListener {
    //view
    private ImageView imageView;
    private ConstraintLayout back;

    // object
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        getIntentData(getIntent());
        initViews();
        loadImage(imageUrl);
    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            imageUrl = intent.getStringExtra(AppConstants.DETAIL_IMAGE);
        }
    }

    private void initViews() {
        imageView = findViewById(R.id.detailItemImageId);
        back = findViewById(R.id.backLayoutId);
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

    private void loadImage(String imageUrl) {
        if (imageUrl != null) {
            Glide
                    .with(this)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    private void handleBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
