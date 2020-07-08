package com.idam.idam_tech.activities.notification;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.auth.LoginActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterNotification;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Notification;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifActivity extends AppCompatActivity {
    List<Notification> listNotif;
    RecyclerView rv_notif;
    LinearLayout btn_back;

    public void findVariabel(){
        listNotif = new ArrayList<>();
        rv_notif = findViewById(R.id.rv_notif);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        findVariabel();
        jsonRequest();
        returnback();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement=new SessionManagement(NotifActivity.this);
        int userID = sessionManagement.getSession();
        if (userID != -1){

        }else{
            Intent intent=new Intent(NotifActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void returnback() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(NotifActivity.this, MainActivity.class);
                i.putExtra("firstOpen","home");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(NotifActivity.this, MainActivity.class);
        i.putExtra("firstOpen","home");
        startActivity(i);
    }

    private void jsonRequest() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"getNotif",
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
                                String waktu = jObj.getString("waktu");
                                Log.d("NOTIFICATION", waktu);
                                Notification notification = new Notification();
                                notification.setId(jObj.getString("id"));
                                notification.setNama(jObj.getString("nama"));
                                notification.setDeskripsi(jObj.getString("deskripsi"));
                                notification.setWaktu(jObj.getString("waktu"));
                                notification.setStatus(jObj.getString("status"));
                                notification.setKategori(jObj.getString("kategori"));
                                listNotif.add(notification);
                            }

                        }catch (Exception error){
                            Toast.makeText(NotifActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerview(listNotif);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotifActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(NotifActivity.this);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setuprecyclerview(List<Notification> listHistori) {
        RecyclerViewAdapterNotification adapterNotification= new RecyclerViewAdapterNotification(this, listNotif);
        rv_notif.setLayoutManager(new LinearLayoutManager(this));
        rv_notif.setAdapter(adapterNotification);
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
