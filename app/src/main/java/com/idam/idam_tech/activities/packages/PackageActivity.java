package com.idam.idam_tech.activities.packages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterPackages;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.PackageHistory;
import com.idam.idam_tech.models.Packages;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PackageActivity extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    List<Packages> listPackages;
    RecyclerView rv_packages;
    Button btn_buy;
    LinearLayout btn_back;

    public void findVariabel(){
        listPackages = new ArrayList<>();
        rv_packages = findViewById(R.id.rv_packages);
        btn_buy = findViewById(R.id.btn_buy);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        findVariabel();
        jsonRequest();
        returnBack();

        final String promo_id = getIntent().getStringExtra("promo_id");
        Log.d("promo_id : ", promo_id);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(PackageActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        i.putExtra("firstOpen","home");
        startActivity(i);
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PackageActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);;
                i.putExtra("firstOpen","home");
                startActivity(i);
            }
        });
    }

    private void jsonRequest() {
        link link=new link();
        String BASE_URL = link.getBASE_URL();
        request = new JsonArrayRequest(BASE_URL+"packages", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                for (int i=0; i<response.length(); i++ ){
                    try {
                        jsonObject = response.getJSONObject(i);
                        Packages packages = new Packages();
                        packages.setId(jsonObject.getString("id"));
                        packages.setNama(jsonObject.getString("nama"));
                        packages.setJumlah_hari(jsonObject.getString("jumlah_hari")+" Day");

                        String harga= jsonObject.getString("harga");
                        packages.setHarga(harga);
                        listPackages.add(packages);

                    }catch (Exception e){
                        Toast.makeText(PackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                    }
                }
                setuprecyclerview(listPackages);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(PackageActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Packages> listPackages) {
        RecyclerViewAdapterPackages adapterPackages= new RecyclerViewAdapterPackages(this, listPackages);
        rv_packages.setLayoutManager(new LinearLayoutManager(this));
        rv_packages.setAdapter(adapterPackages);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);;
        i.putExtra("firstOpen","home");
        startActivity(i);
        finish();
        return true;
    }
}
