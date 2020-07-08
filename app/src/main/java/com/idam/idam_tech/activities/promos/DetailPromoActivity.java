package com.idam.idam_tech.activities.promos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.packages.DetailPackageActivity;
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterSyaratPaket;
import com.idam.idam_tech.adapters.RecyclerViewAdapterSyaratPromo;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Promo;
import com.idam.idam_tech.models.SyaratPaket;
import com.idam.idam_tech.models.SyaratPromo;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPromoActivity extends AppCompatActivity {

    Button btn_gunakan;
    ImageView gambar_promo;
    TextView kode_promo, deskripsi_promo, nama_promo, diskon;

    List<SyaratPromo> listSyaratPromo;
    RecyclerView rv_syarat_promo;
    LinearLayout btn_back;

    public void findVariabel(){
        btn_gunakan = (Button) findViewById(R.id.btn_gunakan);
        gambar_promo = (ImageView) findViewById(R.id.gambar_promo);
        kode_promo = (TextView) findViewById(R.id.kode_promo);
        deskripsi_promo = (TextView) findViewById(R.id.deskripsi_promo);
        nama_promo = (TextView) findViewById(R.id.nama_promo);
        diskon = (TextView) findViewById(R.id.diskon);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        listSyaratPromo = new ArrayList<>();
        rv_syarat_promo = findViewById(R.id.rv_syarat_promo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);

        findVariabel();
        getDetailPromo();

        String firstOpen = getIntent().getStringExtra("firstOpen");
        final String promo_id = getIntent().getStringExtra("promo_id");
        final String kode_promo = getIntent().getStringExtra("kode_promo");
        final String package_id = getIntent().getStringExtra("package_id");

        Log.d("firstOpen : ", firstOpen);
        Log.d("promo_id : ", promo_id);
        Log.d("package_id : ", package_id);
        if (firstOpen.equals("detailPaket")){
            btn_gunakan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(DetailPromoActivity.this, DetailPackageActivity.class);
                    i.putExtra("promo_id", promo_id);
                    i.putExtra("firstOpen", "promoGunakan");
                    i.putExtra("package_id", package_id);
                    i.putExtra("kode_promo", kode_promo);
                    startActivity(i);
                }
            });
        }else if(firstOpen.equals("home")){
            btn_gunakan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(DetailPromoActivity.this, PackageActivity.class);
                    i.putExtra("promo_id", promo_id);
                    startActivity(i);
                }
            });
        }
        jsonRequestSyaratPromo();

        returnBack();

    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstOpen = getIntent().getStringExtra("firstOpen");
                final String package_id = getIntent().getStringExtra("package_id");
                if (firstOpen.equals("detailPaket")){
                    Intent i=new Intent(DetailPromoActivity.this, PromoActivity.class);
                    i.putExtra("firstBack","0");
                    i.putExtra("promo_id", "0");
                    i.putExtra("package_id", package_id);
                    i.putExtra("firstOpen", "home");
                    startActivity(i);
                    finish();
                }else if(firstOpen.equals("home")){
                    Intent i=new Intent(DetailPromoActivity.this, MainActivity.class);
                    i.putExtra("promo_id", "0");
                    i.putExtra("firstOpen", "home");
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void getDetailPromo() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        link link = new link();
        final String BASE_URL = link.getBASE_URL();
        final String BASE_IMAGE = link.getURL_IMAGE_PROMO();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"getDetailPromo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            kode_promo.setText(jsonObject.getString("kode_promo"));
                            nama_promo.setText(jsonObject.getString("nama_promo"));
                            deskripsi_promo.setText(jsonObject.getString("deskripsi_promo"));
                            diskon.setText("Diskon "+jsonObject.getString("diskon")+"%");
                            Glide.with(DetailPromoActivity.this)
                                    .asBitmap()
                                    .load(BASE_IMAGE+jsonObject.getString("gambar_promo"))
                                    .into(gambar_promo);


                        }catch (Exception error){
                            progressDialog.dismiss();
                            Toast.makeText(DetailPromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailPromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final String promo_id = getIntent().getStringExtra("promo_id");
                Map<String, String> params = new HashMap<>();
                params.put("id", promo_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        String firstOpen = getIntent().getStringExtra("firstOpen");
        final String package_id = getIntent().getStringExtra("package_id");
        if (firstOpen.equals("detailPaket")){
            Intent i=new Intent(this, PromoActivity.class);
            i.putExtra("firstBack","0");
            i.putExtra("promo_id", "0");
            i.putExtra("package_id", package_id);
            i.putExtra("firstOpen", "home");
            startActivity(i);
            finish();
        }else if(firstOpen.equals("home")){
            Intent i=new Intent(this, MainActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen", "home");
            startActivity(i);
            finish();
        }
    }

    private void jsonRequestSyaratPromo() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, BASE_URL+"getSyaratPromo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            Log.d("Data : ",jsonObject.toString());
                            JSONArray ja_data = jsonObject.getJSONArray("data");
                            int length = ja_data.length();

                            Log.d("Jumlah :", String.valueOf(length));

                            for(int i=0; i<length; i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                SyaratPromo syaratPromo = new SyaratPromo();
                                syaratPromo.setId(jObj.getString("id"));
                                syaratPromo.setKeterangan(jObj.getString("keterangan"));
                                syaratPromo.setJumlah(length);
                                listSyaratPromo.add(syaratPromo);
                            }

                        }catch (Exception error){
                            Toast.makeText(DetailPromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerview(listSyaratPromo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setuprecyclerview(List<SyaratPromo> listSyaratPromo) {
        RecyclerViewAdapterSyaratPromo adapterHistoriMember= new RecyclerViewAdapterSyaratPromo(this, listSyaratPromo);
        rv_syarat_promo.setLayoutManager(new LinearLayoutManager(this));
        rv_syarat_promo.setAdapter(adapterHistoriMember);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String firstOpen = getIntent().getStringExtra("firstOpen");
        final String package_id = getIntent().getStringExtra("package_id");
        if (firstOpen.equals("detailPaket")){
            Intent i=new Intent(this, PromoActivity.class);
            i.putExtra("firstBack","0");
            i.putExtra("promo_id", "0");
            i.putExtra("package_id", package_id);
            i.putExtra("firstOpen", "home");
            startActivity(i);
            finish();
        }else if(firstOpen.equals("home")){
            Intent i=new Intent(this, MainActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen", "home");
            startActivity(i);
            finish();
        }
        return true;
    }
}
