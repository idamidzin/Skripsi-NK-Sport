package com.idam.idam_tech.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.admin.MainAdminActivity;
import com.idam.idam_tech.activities.fitnesstools.AlatFitnessActivity;
import com.idam.idam_tech.activities.notification.NotifActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.member.fragment.HomeFragment;
import com.idam.idam_tech.member.fragment.ProfileFragment;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView action_bar_title;
    ImageView btn_notif;
    SwipeRefreshLayout swipe;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("NK-sPoRt");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        action_bar_title = (TextView) findViewById(R.id.action_bar_title);
        btn_notif = (ImageView) findViewById(R.id.btn_notif);
        btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notif_out_white));

        btn_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotifActivity.class));
            }
        });

        SessionManagement sessionManagement=new SessionManagement(this);
        int userID = sessionManagement.getSession();

        String firstOpen = getIntent().getStringExtra("firstOpen");
        if (firstOpen.equals("profile"))
        {
            ProfileFragment profile = new ProfileFragment();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.getMenu().getItem(1).setChecked(false);
            jsonRequestNotifBlack();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profile).commit();
        }
        if(firstOpen.equals("home"))
        {
            HomeFragment fragtry = new HomeFragment();
            jsonRequestNotifWhite();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragtry).commit();
        }else{
            HomeFragment fragtry = new HomeFragment();
            jsonRequestNotifWhite();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragtry).commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this,
                "Tidak di ijinkan menekan tombol back di perangkatmu",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void jsonRequestNotifWhite() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"cekNotif",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("Data Notif: ",jsonObject.getString("data"));
                            JSONArray ja_data = jsonObject.getJSONArray("data");
                            final int length = ja_data.length();
                            Log.d("Jumlah notif :", String.valueOf(length));
                            if(length>=1){
                                btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notif_in_white));
                            }else if(length<=0){
                                btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notif_out_white));
                            }
                        }catch (Exception error){
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(MainActivity.this);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);
    }

    private void jsonRequestNotifBlack() {
        link link_links = new link();
        final String BASE_URLs = link_links.getBASE_URL();
        StringRequest stringRequests=new StringRequest(Request.Method.POST, BASE_URLs+"cekNotif",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray ja_data = jsonObject.getJSONArray("data");
                            int length = ja_data.length();
                            if(length>=1){
                                btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notif_in_black));
                            }else if(length<=0){
                                btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notif_out_black));
                            }
                        }catch (Exception error){
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(MainActivity.this);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queues = Volley.newRequestQueue(MainActivity.this);
        queues.add(stringRequests);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home :
                            HomeFragment home = new HomeFragment();
                            selectedFragment = home;
                            action_bar_title.setTextColor(getResources().getColor(R.color.colorWhite));
//                            btn_notif.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
                            jsonRequestNotifWhite();
                            break;

                        case R.id.nav_profile:
                            ProfileFragment profile = new ProfileFragment();
                            selectedFragment = profile;
                            action_bar_title.setTextColor(getResources().getColor(R.color.colorBlack));
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
                            jsonRequestNotifBlack();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                            selectedFragment).commit();
                    return true;
                }
            };
}
