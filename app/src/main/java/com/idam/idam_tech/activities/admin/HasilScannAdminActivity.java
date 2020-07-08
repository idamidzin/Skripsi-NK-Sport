package com.idam.idam_tech.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Histori;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HasilScannAdminActivity extends AppCompatActivity {

    TextView nik, nama, paket, waktu_masuk,sisa_waktu, histori_id;
    Button btn_checkin, btn_checkout;
    LinearLayout btn_back;

    public void findVariable(){
        nik = (TextView) findViewById(R.id.nik);
        nama = (TextView) findViewById(R.id.nama);
        paket = (TextView) findViewById(R.id.paket);
        waktu_masuk = (TextView) findViewById(R.id.waktu_masuk);
        sisa_waktu = (TextView) findViewById(R.id.sisa_waktu);
        histori_id = (TextView) findViewById(R.id.histori_id);
        btn_checkin = (Button) findViewById(R.id.btn_checkin);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scann_admin);
        findVariable();
        getDetailMember();
        savePresensiCheckIn();
        savePresensiCheckOut();
        returnBack();
    }

    private void returnBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HasilScannAdminActivity.this, ScannerAdminActivity.class));
            }
        });
    }

    private void savePresensiCheckOut() {
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_out();
            }
        });
    }

    private void savePresensiCheckIn() {
        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_in();
            }
        });
    }

    private void check_in(){
        final String id = getIntent().getStringExtra("id");
        final String package_histori_id = (String) histori_id.getText();
        final String waktuIn = (String) waktu_masuk.getText();
        Log.d("id member", id);
        Log.d("histori_id", package_histori_id);
        Log.d("waktu", waktuIn);
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"checkIn",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            final String messages = jsonObject.getString("messages");

                            Toast.makeText(HasilScannAdminActivity.this, "Checkin Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HasilScannAdminActivity.this, MainAdminActivity.class));

                        }catch (Exception error){
                            Toast.makeText(HasilScannAdminActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Histori histori=new Histori();
                        Toast.makeText(HasilScannAdminActivity.this, "FAILED PISAN"+histori.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_id", id);
                params.put("package_histori_id", package_histori_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }

    private void check_out(){
        final String id = getIntent().getStringExtra("id");
        final String package_histori_id = (String) histori_id.getText();
        final String waktuIn = (String) waktu_masuk.getText();
        Log.d("id member", id);
        Log.d("histori_id", package_histori_id);
        Log.d("waktu", waktuIn);
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"checkOut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            final String messages = jsonObject.getString("messages");
                            Histori histori=new Histori();

                            Toast.makeText(HasilScannAdminActivity.this, "Check Out "+messages, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HasilScannAdminActivity.this, MainAdminActivity.class));

                        }catch (Exception error){
                            Histori histori=new Histori();
                            Toast.makeText(HasilScannAdminActivity.this, "FAILED"+histori.getId(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Histori histori=new Histori();
                        Toast.makeText(HasilScannAdminActivity.this, "FAILED PISAN"+histori.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("member_id", id);
                params.put("package_histori_id", package_histori_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }

    private void getDetailMember() {
        final String id = getIntent().getStringExtra("id");
        Log.d("id member", id);
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"getDetailMember",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            final String messages = jsonObject.getString("messages");
                            final String member_id = jsonObject.getString("messages");
                            histori_id.setText(jsonObject.getString("package_histori_id"));
                            nik.setText(jsonObject.getString("nik"));
                            nama.setText(jsonObject.getString("nama_lengkap"));
                            paket.setText(jsonObject.getString("paket"));
                            waktu_masuk.setText(jsonObject.getString("waktu_masuk"));
                            sisa_waktu.setText(jsonObject.getString("sisa_waktu")+" Hari lagi");

                            final String package_histori_id = jsonObject.getString("package_histori_id");
                            Histori histori=new Histori();
                            histori.setId(package_histori_id);
                            Log.d("Package histori id", histori.getId());

                        }catch (Exception error){
                            Toast.makeText(HasilScannAdminActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HasilScannAdminActivity.this, "FAILED PISAN", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }
}
