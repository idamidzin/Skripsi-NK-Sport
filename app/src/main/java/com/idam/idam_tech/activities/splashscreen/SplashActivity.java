package com.idam.idam_tech.activities.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.auth.LoginActivity;
import com.idam.idam_tech.activities.notification.NotifActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
// TOKEN : d60n3EqjTJietcKSkoXbEZ:APA91bG6lomUQSenCUXlo5sgDApq-vmDwq7PBDb0KDl-N6uSGcsHZZV3AQjkMs8bur4-BWkgWuRy66lbZrt0NAbsyBS3bW7wCLADQ7wi0E-Fz7mt--zDpOWvW_AhPimqQ2Al3ubTpw4G

//        Log.d("Token", ""+ FirebaseInstanceId.getInstance().getToken());
//        FirebaseMessaging.getInstance().subscribeToTopic("allDevice");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String firstOpen = getIntent().getStringExtra("firstOpen");

//        Toast.makeText(SplashActivity.this, "Get : "+firstOpen, Toast.LENGTH_SHORT).show();

        Handler handler=new Handler();
        if (firstOpen == null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(SplashActivity.this, LoginActivity.class);
                    i.putExtra("firstOpen", "home");
                    startActivity(i);
                }
            }, 2000);
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(SplashActivity.this, NotifActivity.class);
                    i.putExtra("promo_id", "0");
                    startActivity(i);
                }
            }, 2000);
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(SplashActivity.this,
                "Tidak di ijinkan menekan tombol back di perangkatmu",
                Toast.LENGTH_SHORT)
                .show();
    }
}
