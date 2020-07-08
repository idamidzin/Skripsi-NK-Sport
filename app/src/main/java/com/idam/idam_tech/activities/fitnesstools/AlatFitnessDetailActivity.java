package com.idam.idam_tech.activities.fitnesstools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.histori.HistoriActivity;
import com.idam.idam_tech.activities.promos.DetailPromoActivity;
import com.idam.idam_tech.api.link;

public class AlatFitnessDetailActivity extends AppCompatActivity {

    ImageView gambar_alat;
    TextView nama_alat, deskripsi_alat;
    LinearLayout btn_back;

    public void findVariable(){
        gambar_alat = (ImageView) findViewById(R.id.gambar_alat);
        nama_alat = (TextView) findViewById(R.id.nama_alat);
        deskripsi_alat = (TextView) findViewById(R.id.deskripsi_alat);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alat_fitness_detail);
        findVariable();
        String nama = getIntent().getStringExtra("nama");
        String gambar = getIntent().getStringExtra("gambar");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        link link = new link();
        final String BASE_IMAGE = link.getURL_IMAGE_ALAT();
        Glide.with(AlatFitnessDetailActivity.this)
                .asBitmap()
                .load(BASE_IMAGE+gambar)
                .into(gambar_alat);
        nama_alat.setText(nama);
        deskripsi_alat.setText(deskripsi);
        returnBack();
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlatFitnessDetailActivity.this, AlatFitnessActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AlatFitnessDetailActivity.this, AlatFitnessActivity.class));
    }
}
