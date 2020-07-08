package com.idam.idam_tech.activities.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;

public class NotifDetailActivity extends AppCompatActivity {

    TextView txtkategori, txtdeskripsi,btn_back;

    public void findVariabel(){
        txtkategori = (TextView) findViewById(R.id.kategori);
        txtdeskripsi = (TextView) findViewById(R.id.deskripsi);
        btn_back = (TextView) findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_detail);
        findVariabel();
        String kategori = getIntent().getStringExtra("kategori");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String id = getIntent().getStringExtra("id");

        txtkategori.setText(kategori);
        txtdeskripsi.setText(deskripsi);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotifDetailActivity.this, NotifActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotifDetailActivity.this, NotifActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
