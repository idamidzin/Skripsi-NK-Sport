package com.idam.idam_tech.activities.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.packages.UploadBuktiActivity;

public class LoadingActivity extends AppCompatActivity {
    Animation loading_buy;
    ImageView deal_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        deal_icon = (ImageView) findViewById(R.id.deal_icon);

        loading_buy = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.loading_buy);
        deal_icon.startAnimation(loading_buy);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Package Id : ", getIntent().getStringExtra("package_id"));
                Intent intent=new Intent(LoadingActivity.this, UploadBuktiActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(LoadingActivity.this,
                "Tidak di ijinkan menekan tombol back di perangkatmu",
                Toast.LENGTH_SHORT)
                .show();
    }
}
