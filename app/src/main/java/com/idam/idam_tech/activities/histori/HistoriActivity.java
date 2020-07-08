package com.idam.idam_tech.activities.histori;

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

import com.android.volley.AuthFailureError;
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
import com.idam.idam_tech.adapters.RecyclerViewAdapterHistoriMember;
import com.idam.idam_tech.adapters.RecyclerViewAdapterPackages;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Histori;
import com.idam.idam_tech.models.Packages;
import com.idam.idam_tech.models.Promo;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class HistoriActivity extends AppCompatActivity {
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    List<Histori> listHistori;
    RecyclerView rv_histori;
    LinearLayout btn_back;

    public void findVariabel(){
        listHistori = new ArrayList<>();
        rv_histori = findViewById(R.id.rv_histori);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);
        findVariabel();
        jsonRequest();
        returnBack();
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HistoriActivity.this, MainActivity.class);
                i.putExtra("firstOpen","home");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HistoriActivity.this, MainActivity.class);
        i.putExtra("firstOpen", "home");
        startActivity(i);
    }

    private void jsonRequest() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"getHistoriPackage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            Log.d("Data : ",jsonObject.toString());
                            JSONArray ja_data = jsonObject.getJSONArray("histori");
                            int length = ja_data.length();

                            Log.d("Jumlah :", String.valueOf(length));

                            for(int i=0; i<length; i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                String status = jObj.getString("status_baca");
                                Log.d("status", status);
                                if (status == NULL){

                                }
                                Histori histori = new Histori();
                                histori.setId(jObj.getString("id"));
                                histori.setMember_id(jObj.getString("member_id"));
                                histori.setPackage_id(jObj.getString("package_id"));
                                histori.setStatus_history(jObj.getString("status_history"));
                                histori.setStatus_baca(jObj.getString("status_baca"));
                                histori.setTanggal_mulai(jObj.getString("tanggal_mulai"));
                                listHistori.add(histori);
                            }

                        }catch (Exception error){
                            Toast.makeText(HistoriActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerview(listHistori);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoriActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(HistoriActivity.this);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setuprecyclerview(List<Histori> listHistori) {
        RecyclerViewAdapterHistoriMember adapterHistoriMember= new RecyclerViewAdapterHistoriMember(this, listHistori);
        rv_histori.setLayoutManager(new LinearLayoutManager(this));
        rv_histori.setAdapter(adapterHistoriMember);
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
