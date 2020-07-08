package com.idam.idam_tech.activities.promos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.packages.DetailPackageActivity;
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterPromoHorizontal;
import com.idam.idam_tech.adapters.RecyclerViewAdapterPromoVertical;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Promo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromoActivity extends AppCompatActivity {
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    List<Promo> listPromo;
    RecyclerView rv_promo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        listPromo = new ArrayList<>();
        rv_promo = findViewById(R.id.rv_promo);
        jsonRequestPromo();
        Log.d("package_id ooyy", getIntent().getStringExtra("package_id"));

    }

    @Override
    public void onBackPressed() {
        String firstBack = getIntent().getStringExtra("firstBack");
        String package_id = getIntent().getStringExtra("package_id");
        if (firstBack.equals("detailPackage")){
            Intent i=new Intent(this, DetailPackageActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen","promoBack");
            i.putExtra("package_id", package_id);
            startActivity(i);
        }else if (firstBack.equals("home")){
            Intent i=new Intent(this, MainActivity.class);
            i.putExtra("firstOpen","home");
            startActivity(i);
            finish();
        }else{
            Intent i=new Intent(this, DetailPackageActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen","promoBack");
            i.putExtra("package_id", package_id);
            startActivity(i);
        }
    }

    private void jsonRequestPromo() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, BASE_URL+"promos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObj=new JSONObject(response);

                            JSONArray ja_data = jsonObj.getJSONArray("data");
                            int length = ja_data.length();

                            Log.d("Jumlah :", String.valueOf(length));

                            for(int i=0; i<length; i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                Promo promo = new Promo();
                                promo.setId(jObj.getString("id"));
                                promo.setKode_promo(jObj.getString("kode_promo"));
                                promo.setNama(jObj.getString("nama_promo"));
                                promo.setGambar(jObj.getString("gambar_promo"));
                                promo.setDeskripsi(jObj.getString("deskripsi_promo"));
                                promo.setStatus(jObj.getString("status_promo"));
                                promo.setPackage_id(getIntent().getStringExtra("package_id"));
                                listPromo.add(promo);
                            }
                        }catch (Exception error){
                            Toast.makeText(PromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerviewPromo(listPromo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setuprecyclerviewPromo(List<Promo> listAlat) {
        RecyclerViewAdapterPromoVertical adapterPromo= new RecyclerViewAdapterPromoVertical(PromoActivity.this, listAlat);
        rv_promo.setLayoutManager(new LinearLayoutManager(PromoActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_promo.setAdapter(adapterPromo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String firstBack = getIntent().getStringExtra("firstBack");
        String package_id = getIntent().getStringExtra("package_id");
        if (firstBack.equals("detailPackage")){
            Intent i=new Intent(this, DetailPackageActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen","promoBack");
            i.putExtra("package_id", package_id);
            startActivity(i);
        }else if (firstBack.equals("home")){
            Intent i=new Intent(this, MainActivity.class);
            i.putExtra("firstOpen","home");
            startActivity(i);
            finish();
        }else{
            Intent i=new Intent(this, DetailPackageActivity.class);
            i.putExtra("promo_id", "0");
            i.putExtra("firstOpen","promoBack");
            i.putExtra("package_id", package_id);
            startActivity(i);
        }
        return true;
    }
}
