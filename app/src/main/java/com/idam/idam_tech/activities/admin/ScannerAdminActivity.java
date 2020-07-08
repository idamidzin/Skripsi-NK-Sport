package com.idam.idam_tech.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.auth.LoginActivity;
import com.idam.idam_tech.api.link;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerAdminActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_scanner_admin);

        gotoCameraScann();
    }

    private void gotoCameraScann() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(ScannerAdminActivity.this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.startCamera();
    }


    @Override
    public void handleResult(Result result) {
        final String nik = result.getText();
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"cekMember",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");

                            if (messages.equals("sukses")){
                                Toast.makeText(ScannerAdminActivity.this, "Anda Member", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(ScannerAdminActivity.this, HasilScannAdminActivity.class);
                                i.putExtra("id",jsonObject.getString("id"));
                                startActivity(i);
                            }

                        }catch (Exception error){
                            Toast.makeText(ScannerAdminActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(ScannerAdminActivity.this, LoginActivity.class);
                            startActivity(i);
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScannerAdminActivity.this, "FAILED PISAN", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
        zXingScannerView.resumeCameraPreview(this);
    }

    private void cekMember(String id) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this, MainAdminActivity.class);
        startActivity(i);
        finish();
        return super.onOptionsItemSelected(item);
        }
    }

