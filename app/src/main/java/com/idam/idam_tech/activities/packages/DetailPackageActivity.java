package com.idam.idam_tech.activities.packages;

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
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.promos.PromoActivity;
import com.idam.idam_tech.activities.splashscreen.LoadingActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterSyaratPaket;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.PackageHistory;
import com.idam.idam_tech.models.Promo;
import com.idam.idam_tech.models.SyaratPaket;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPackageActivity extends AppCompatActivity {

    private static final String TAG= DetailPackageActivity.class.getSimpleName();

    TextView harga, harga_detail, jumlah_hari, harga_diskon, btn_hemat;
    Button btn_buy;
    ImageView background_harga;

    List<SyaratPaket> listSyaratPaket;
    RecyclerView rv_syarat_paket;
    LinearLayout btn_back;

    public void findVariable(){
        harga = (TextView) findViewById(R.id.harga);
        harga_detail = (TextView) findViewById(R.id.harga_detail);
        jumlah_hari = (TextView) findViewById(R.id.jumlah_hari);
        harga_diskon = (TextView) findViewById(R.id.harga_diskon);
        btn_hemat = (TextView) findViewById(R.id.btn_hemat);

        btn_buy = (Button) findViewById(R.id.btn_buy);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);

        background_harga = (ImageView) findViewById(R.id.background_harga);

        listSyaratPaket = new ArrayList<>();
        rv_syarat_paket = findViewById(R.id.rv_syarat_paket);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_package);
        findVariable();

        String package_id = getIntent().getStringExtra("package_id");
        DetailPackage();
        cekPromoMember();
        moveToProses();
        jsonRequest();
        returnBack();

        harga_diskon.setVisibility(View.GONE);
        background_harga.setVisibility(View.GONE);
        btn_hemat.setBackground(getResources().getDrawable(R.drawable.button_hemat_not));
        btn_hemat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_discount_not, 0, 0, 0);
        Promo promo = new Promo();
        promo.setPackage_id(getIntent().getStringExtra("package_id"));

        final String promo_id = getIntent().getStringExtra("promo_id");
        final String kode_promo = getIntent().getStringExtra("kode_promo");

        String firstOpen = getIntent().getStringExtra("firstOpen");
        if (firstOpen.equals("loading")){
            btn_buy.setVisibility(View.GONE);
            btn_hemat.setText(""+promo_id);
        }else if(firstOpen.equals("promoGunakan")){
            btn_buy.setVisibility(View.VISIBLE);
            btn_hemat.setText(""+kode_promo);
        }else if(firstOpen.equals("promoBack")){
            btn_buy.setVisibility(View.VISIBLE);
        }else if(firstOpen.equals("prosesBack")){
            btn_buy.setVisibility(View.VISIBLE);
        }
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DetailPackageActivity.this, PackageActivity.class);
                i.putExtra("promo_id", "0");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(DetailPackageActivity.this, PackageActivity.class);
        i.putExtra("promo_id", "0");
        startActivity(i);
    }

    private void cekPromoMember() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"cekPromoMember",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("message promo", jsonObject.getString("messages"));
                            String messages = jsonObject.getString("messages");
                            if(messages.equals("sukses")){
                                btn_hemat.setBackground(getResources().getDrawable(R.drawable.button_hemat));
                                btn_hemat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_discount, 0, 0, 0);
                                moveToPromoMember();
                            }else if(messages.equals("gagal")){
                                btn_hemat.setBackground(getResources().getDrawable(R.drawable.button_hemat_not));
                                btn_hemat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_discount_not, 0, 0, 0);
                            }
                            Log.d("Data Promo Member: ",jsonObject.toString());
                        }catch (Exception error){
                            Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(DetailPackageActivity.this);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void moveToPromoMember() {
        btn_hemat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String package_id = getIntent().getStringExtra("package_id");
                Intent i=new Intent(DetailPackageActivity.this, PromoActivity.class);
                i.putExtra("firstBack","detailPackage");
                i.putExtra("package_id", package_id);
                startActivity(i);
            }
        });
    }


    private void moveToProses() {
        final String promo_id = getIntent().getStringExtra("promo_id");
        if(promo_id.equals("0")){
            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String promo_id = getIntent().getStringExtra("promo_id");
                    final String kode_promo = getIntent().getStringExtra("kode_promo");
                    Intent intent=new Intent(DetailPackageActivity.this, ProsesPackageActivity.class);
                    intent.putExtra("package_id",getIntent().getStringExtra("package_id"));
                    intent.putExtra("promo_id", "0");
                    intent.putExtra("kode_promo", kode_promo);
                    intent.putExtra("harga_package", harga.getText());
                    intent.putExtra("jumlah_hari", jumlah_hari.getText());
                    startActivity(intent);
                }
            });
        }else{
            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String promo_id = getIntent().getStringExtra("promo_id");
                    final String kode_promo = getIntent().getStringExtra("kode_promo");
                    Intent intent=new Intent(DetailPackageActivity.this, ProsesPackageActivity.class);
                    intent.putExtra("package_id",getIntent().getStringExtra("package_id"));
                    intent.putExtra("promo_id", promo_id);
                    intent.putExtra("kode_promo", kode_promo);
                    intent.putExtra("harga_package", harga.getText());
                    intent.putExtra("jumlah_hari", jumlah_hari.getText());
                    startActivity(intent);
                }
            });
        }

    }

    private void jsonRequest() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, BASE_URL+"getSyaratPaket",
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
                                SyaratPaket syaratPaket = new SyaratPaket();
                                syaratPaket.setId(jObj.getString("id"));
                                syaratPaket.setKeterangan(jObj.getString("keterangan"));
                                syaratPaket.setJumlah(length);
                                listSyaratPaket.add(syaratPaket);
                            }

                        }catch (Exception error){
                            Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerview(listSyaratPaket);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setuprecyclerview(List<SyaratPaket> listHistori) {
        RecyclerViewAdapterSyaratPaket adapterHistoriMember= new RecyclerViewAdapterSyaratPaket(this, listSyaratPaket);
        rv_syarat_paket.setLayoutManager(new LinearLayoutManager(this));
        rv_syarat_paket.setAdapter(adapterHistoriMember);
    }

    private void DetailPackage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        link link = new link();
        final String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"getDetailPackage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            harga_detail.setText(jsonObject.getString("harga"));
                            jumlah_hari.setText(jsonObject.getString("jumlah_hari")+" Hari");

                            String hargas= jsonObject.getString("harga");
                            harga.setText(hargas);

                        }catch (Exception error){
                            progressDialog.dismiss();
                            Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String package_id = getIntent().getStringExtra("package_id");
                Map<String, String> params = new HashMap<>();
                params.put("id", package_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this, PackageActivity.class);
        i.putExtra("promo_id", "0");
        startActivity(i);
        finish();
        return true;
    }
}
