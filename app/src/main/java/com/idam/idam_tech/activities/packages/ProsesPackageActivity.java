package com.idam.idam_tech.activities.packages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.idam.idam_tech.activities.promos.DetailPromoActivity;
import com.idam.idam_tech.activities.splashscreen.LoadingActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.PackageHistory;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProsesPackageActivity extends AppCompatActivity {

    TextView title, harga_normal, harga_diskon, jumlah_hari, nama_promo, txtdiskon;
    ImageView image_coret;
    Button btn_beli;
    LinearLayout btn_back;

    public void findVariabel(){
        title = (TextView) findViewById(R.id.title);
        harga_normal = (TextView) findViewById(R.id.harga_normal);
        harga_diskon = (TextView) findViewById(R.id.harga_diskon);
        jumlah_hari = (TextView) findViewById(R.id.jumlah_hari);
        nama_promo = (TextView) findViewById(R.id.nama_promo);
        txtdiskon = (TextView) findViewById(R.id.txtdiskon);
        image_coret = (ImageView) findViewById(R.id.image_coret);
        btn_beli = (Button) findViewById(R.id.btn_beli);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_package);
        findVariabel();

        final String package_id = getIntent().getStringExtra("package_id");
        final String promo_id = getIntent().getStringExtra("promo_id");
        final String harga_package = getIntent().getStringExtra("harga_package");
        final String jumlah = getIntent().getStringExtra("jumlah_hari");
        Log.d("Package ", package_id);
        Log.d("Promo ", promo_id);
        if (promo_id.equals("0")){
            title.setText("Nikmati olahragamu dengan semangat, pastikan terus setia dengan Nk Sport.");
            jumlah_hari.setText(jumlah);
            harga_normal.setText(harga_package);
            image_coret.setVisibility(View.GONE);
            harga_diskon.setVisibility(View.GONE);
            nama_promo.setText("Normal Sale");
            txtdiskon.setText("Rp. "+harga_package);
            btn_beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  CreatePackageHistoryNotPromo();
                }
            });

        }else{
            getDetailPromo();
            btn_beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreatePackageHistoryPromo();
                }
            });
        }

        returnBack();
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String package_id = getIntent().getStringExtra("package_id");
                final String promo_id = getIntent().getStringExtra("promo_id");
                final String kode_promo = getIntent().getStringExtra("kode_promo");
                Intent intent=new Intent(ProsesPackageActivity.this, DetailPackageActivity.class);
                intent.putExtra("package_id", package_id);
                intent.putExtra("promo_id", promo_id);
                intent.putExtra("kode_promo", kode_promo);
                intent.putExtra("firstOpen", "prosesBack");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final String package_id = getIntent().getStringExtra("package_id");
        final String promo_id = getIntent().getStringExtra("promo_id");
        final String kode_promo = getIntent().getStringExtra("kode_promo");
        Intent intent=new Intent(ProsesPackageActivity.this, DetailPackageActivity.class);
        intent.putExtra("package_id", package_id);
        intent.putExtra("promo_id", promo_id);
        intent.putExtra("kode_promo", kode_promo);
        intent.putExtra("firstOpen", "prosesBack");
        startActivity(intent);
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

                            String diskon = jsonObject.getString("diskon");
                            final String package_id = getIntent().getStringExtra("package_id");
                            final String promo_id = getIntent().getStringExtra("promo_id");
                            final String harga_package = getIntent().getStringExtra("harga_package");
                            final String jumlah = getIntent().getStringExtra("jumlah_hari");
                            harga_normal.setText(harga_package);
                            jumlah_hari.setText(jumlah);
                                Log.d("diskon", diskon);

                                int dis = Integer.parseInt(diskon);
                                int harga = Integer.parseInt(harga_package);
                                int persen = harga*dis/100;
                                int total = harga-persen;
                                String hasil = String.valueOf(total);

                                Log.d("Harga Total ", hasil);

                                harga_diskon.setText(hasil);

                                nama_promo.setText(jsonObject.getString("nama_promo"));
                                txtdiskon.setText("Diskon "+jsonObject.getString("diskon")+"%");

                        }catch (Exception error){
                            progressDialog.dismiss();
                            Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
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

    private void CreatePackageHistoryPromo(){
        link link=new link();
        final String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"createPackageHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("messages").equals("sukses")){

                                String id= jsonObject.getString("package_history_id");
                                PackageHistory packageHistory= new PackageHistory(Integer.parseInt(id));
                                PackageHistory sessionPackageHistory=new PackageHistory(ProsesPackageActivity.this);
                                sessionPackageHistory.saveSessionHistory(packageHistory);

                                Intent intent=new Intent(ProsesPackageActivity.this, LoadingActivity.class);
                                intent.putExtra("package_id",getIntent().getStringExtra("package_id"));
                                startActivity(intent);
                            }

                        }catch (Exception error){
                            Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(ProsesPackageActivity.this);
                String member_id = String.valueOf(sessionManagement.getSession());
                String package_id = getIntent().getStringExtra("package_id");
                String promo_id = getIntent().getStringExtra("promo_id");
                Map<String, String> params = new HashMap<>();
                params.put("package_id", package_id);
                params.put("member_id", member_id);
                params.put("promo_id", promo_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CreatePackageHistoryNotPromo(){
        link link=new link();
        final String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"createPackageHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("messages").equals("sukses")){

                                String id= jsonObject.getString("package_history_id");
                                PackageHistory packageHistory= new PackageHistory(Integer.parseInt(id));
                                PackageHistory sessionPackageHistory=new PackageHistory(ProsesPackageActivity.this);
                                sessionPackageHistory.saveSessionHistory(packageHistory);

                                Intent intent=new Intent(ProsesPackageActivity.this, LoadingActivity.class);
                                intent.putExtra("package_id",getIntent().getStringExtra("package_id"));
                                startActivity(intent);
                            }

                        }catch (Exception error){
                            Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProsesPackageActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(ProsesPackageActivity.this);
                String member_id = String.valueOf(sessionManagement.getSession());
                String package_id = getIntent().getStringExtra("package_id");
                Map<String, String> params = new HashMap<>();
                params.put("package_id", package_id);
                params.put("member_id", member_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
