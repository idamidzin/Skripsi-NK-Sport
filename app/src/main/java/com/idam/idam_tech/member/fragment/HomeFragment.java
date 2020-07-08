package com.idam.idam_tech.member.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.notification.NotifActivity;
import com.idam.idam_tech.adapters.RecyclerViewAdapterFitnessToolHorizontal;
import com.idam.idam_tech.adapters.RecyclerViewAdapterPromoHorizontal;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.activities.fitnesstools.AlatFitnessActivity;
import com.idam.idam_tech.activities.histori.HistoriActivity;
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.models.FitnessTool;
import com.idam.idam_tech.models.Notification;
import com.idam.idam_tech.models.Promo;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    TextView txtUsername, txtNik, txtStatusMember,lihat_alatFitness, lihat_promoSpesial;
    public static Button btn_logout;
    ImageView btn_package, btn_fitness, btn_info, btn_notif;
    CircleImageView profile_image;
    RequestOptions option;
    LinearLayout background_home;

    SwipeRefreshLayout swipe_home;

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    List<Promo> listPromo;
    List<FitnessTool> listAlat;
    RecyclerView rv_alatFitness;
    RecyclerView rv_promo;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View viewNotif = inflater.inflate(R.layout.action_bar, container, false);
        txtUsername = (TextView) view.findViewById(R.id.txtUsername);
        txtStatusMember = (TextView) view.findViewById(R.id.txtStatusMember);
        lihat_alatFitness = (TextView) view.findViewById(R.id.lihat_alatFitness);
        lihat_promoSpesial = (TextView) view.findViewById(R.id.lihat_promoSpesial);
        txtNik = (TextView) view.findViewById(R.id.txtNik);
        btn_logout =(Button) view.findViewById(R.id.btn_logout);
        btn_package = (ImageView) view.findViewById(R.id.btn_package);
        btn_fitness = (ImageView) view.findViewById(R.id.btn_fitness);
        btn_info = (ImageView) view.findViewById(R.id.btn_info);
        background_home = (LinearLayout) view.findViewById(R.id.background_home);
        swipe_home = view.findViewById(R.id.swipe_home);

        swipe_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent i=new Intent(getActivity().getApplication(), MainActivity.class);
                i.putExtra("firstOpen","home");
                startActivity(i);
                swipe_home.setRefreshing(false);
            }
        });

        swipe_home.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorWhite));
        swipe_home.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));

        listPromo = new ArrayList<>();
        listAlat = new ArrayList<>();
        rv_alatFitness = view.findViewById(R.id.rv_alatFitness);
        rv_promo = view.findViewById(R.id.rv_promo);

        btn_notif = viewNotif.findViewById(R.id.btn_notif);

        getDetailUser();
        jsonRequestPromo();
        jsonRequestAlat();

        cekBuyPackageHistory();
        background_home.setBackground(getResources().getDrawable(R.drawable.gym_home));

        lihat_alatFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), AlatFitnessActivity.class));
            }
        });

        btn_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity().getApplication(), PackageActivity.class);
                i.putExtra("promo_id", "0");
                startActivity(i);
            }
        });

        btn_fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), AlatFitnessActivity.class));
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(), HistoriActivity.class));
            }
        });

        return view;
    }

    private void jsonRequestAlat() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
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
//                            Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerviewAlat(listAlat);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }
    private void setuprecyclerviewAlat(List<FitnessTool> listAlat) {
        RecyclerViewAdapterFitnessToolHorizontal adapterPromo= new RecyclerViewAdapterFitnessToolHorizontal(getContext(), listAlat);
        rv_alatFitness.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_alatFitness.setAdapter(adapterPromo);
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
                                listPromo.add(promo);
                            }
                        }catch (Exception error){
//                            Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                        setuprecyclerviewPromo(listPromo);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

    private void setuprecyclerviewPromo(List<Promo> listAlat) {
        RecyclerViewAdapterPromoHorizontal adapterPromo= new RecyclerViewAdapterPromoHorizontal(getContext(), listAlat);
        rv_promo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_promo.setAdapter(adapterPromo);
    }

    private void cekBuyPackageHistory() {
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"cekBuyPackageHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String pesan = jsonObject.getString("messages");
                            Log.d("Days", jsonObject.getString("days"));
                            Log.d("Messages :", pesan);

                            if (pesan.equals("sukses")){
                                String days = jsonObject.getString("days");
                                String kondisi = jsonObject.getString("status");
                                if(kondisi.equals("destroy")){
                                    Log.d("Kondisi : ", "DESTROY");
                                    txtStatusMember.setText("Member Off");
                                }else if(kondisi.equals("on")){
                                    Log.d("Kondisi : ", "ON");
                                    txtStatusMember.setText(days+" hari lagi");
                                }else if(kondisi.equals("off")){
                                    Log.d("Kondisi : ", "OFF");
                                    txtStatusMember.setText("Member Off");
                                }
                            }else if(pesan.equals("gagal")){
                                Log.d("Kondisi :", "NEXT");
                                txtStatusMember.setText("Member Off");
                            }

                        }catch (Exception error){
//                            Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(getContext());
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

    public void getDetailUser(){
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"members",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            txtNik.setText(jsonObject.getString("nik"));

                        }catch (Exception error){
                            Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(getContext());
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

}
