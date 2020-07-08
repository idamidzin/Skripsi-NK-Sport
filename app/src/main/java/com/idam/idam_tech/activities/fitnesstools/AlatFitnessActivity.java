package com.idam.idam_tech.activities.fitnesstools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterFitnessToolHorizontal;
import com.idam.idam_tech.adapters.RecyclerViewAdapterFitnessToolVertical;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.FitnessTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlatFitnessActivity extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    List<FitnessTool> listAlat;
    RecyclerView rv_alatFitness;
    LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alat_fitness);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);

        listAlat = new ArrayList<>();
        rv_alatFitness = (RecyclerView) findViewById(R.id.rv_alatFitness);
        jsonRequest();
        returnBack();
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AlatFitnessActivity.this, MainActivity.class);
                i.putExtra("firstOpen","home");
                startActivity(i);
            }
        });
    }

    private void jsonRequest() {
        link link=new link();
        String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, BASE_URL+"fitness_tool",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj=new JSONObject(response);

                            JSONArray ja_data = jsonObj.getJSONArray("data");
                            int length = ja_data.length();

                            Log.d("Jumlah alat :", String.valueOf(length));

                            for(int i=0; i<length; i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                FitnessTool fitnessTool = new FitnessTool();
                                fitnessTool.setId(jObj.getString("id"));
                                fitnessTool.setNama(jObj.getString("nama"));
                                fitnessTool.setGambar(jObj.getString("gambar"));
                                fitnessTool.setDeskripsi(jObj.getString("deskripsi"));
                                fitnessTool.setStatus(jObj.getString("status"));
                                listAlat.add(fitnessTool);
                            }
                        }catch (Exception error){
                            Toast.makeText(AlatFitnessActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerviewAlat(listAlat);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AlatFitnessActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


    private void setuprecyclerviewAlat(List<FitnessTool> listAlat) {
        RecyclerViewAdapterFitnessToolVertical adapterFitnessTool= new RecyclerViewAdapterFitnessToolVertical(this, listAlat);
        rv_alatFitness.setLayoutManager(new LinearLayoutManager(this));
        rv_alatFitness.setAdapter(adapterFitnessTool);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("firstOpen","home");
        startActivity(i);
        finish();
        return true;
    }
}
