package com.csi.technodrugs.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.csi.technodrugs.R;

public class ActivityAbout extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageViewCsiLogo;
    Animation animationFade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        animationFade = AnimationUtils.loadAnimation(ActivityAbout.this, R.anim.fade);
        initToolBar();
        initUI();
        imageViewCsiLogo.setAnimation(animationFade);
    }

    private void initUI() {
        imageViewCsiLogo = (ImageView) findViewById(R.id.imageViewCsiLogo);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
