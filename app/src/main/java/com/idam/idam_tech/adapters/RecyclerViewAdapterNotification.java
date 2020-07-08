package com.idam.idam_tech.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.fitnesstools.AlatFitnessActivity;
import com.idam.idam_tech.activities.notification.NotifActivity;
import com.idam.idam_tech.activities.notification.NotifDetailActivity;
import com.idam.idam_tech.activities.packages.DetailPackageActivity;
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.activities.packages.UploadBuktiActivity;
import com.idam.idam_tech.activities.promos.PromoActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Histori;
import com.idam.idam_tech.models.Notification;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewAdapterNotification extends RecyclerView.Adapter<RecyclerViewAdapterNotification.MyViewHolder> {

    private Context mContext;
    private List<Notification> mData;

    public RecyclerViewAdapterNotification(Context mContext, List<Notification> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterNotification.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_notif,parent,false);

        return new RecyclerViewAdapterNotification.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.nama_notif.setText(mData.get(position).getNama());
        holder.tanggal_notif.setText(mData.get(position).getWaktu());
        holder.deskripsi_notif.setText(mData.get(position).getDeskripsi());
        final String notif_id = mData.get(position).getId();
        holder.box_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link link_link = new link();
                final String BASE_URL = link_link.getBASE_URL();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"changeNotif",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject=new JSONObject(response);

                                    Log.d("Data : ",jsonObject.toString());
                                    Log.d("Kategori : ",mData.get(position).getKategori());
                                    String kategori = mData.get(position).getKategori();
                                    if (kategori.equals("promo")){
                                        Intent intent=new Intent(mContext, MainActivity.class);
                                        intent.putExtra("firstOpen","home");
                                        mContext.startActivity(intent);
                                    }else if(kategori.equals("paket")){
                                        Intent intent=new Intent(mContext, PackageActivity.class);
                                        intent.putExtra("promo_id", "0");
                                        mContext.startActivity(intent);
                                    }else if(kategori.equals("alat")) {
                                        Intent intent = new Intent(mContext, AlatFitnessActivity.class);
                                        mContext.startActivity(intent);
                                    }else if(kategori.equals("info")) {
                                        String id= mData.get(position).getId();
                                        String deskripsi= mData.get(position).getDeskripsi();
                                        Intent intent = new Intent(mContext, NotifDetailActivity.class);
                                        intent.putExtra("kategori","Transaksi Paket");
                                        intent.putExtra("deskripsi",deskripsi);
                                        intent.putExtra("id",id);
                                        mContext.startActivity(intent);
                                    }

                                }catch (Exception error){
                                    error.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", notif_id);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(mContext);
                queue.add(stringRequest);
            }
        });
        if (mData.get(position).getStatus().equals("0")){
            holder.card_view.setBackgroundResource(R.drawable.card_list_notif_in);
        }else{
            holder.card_view.setBackgroundResource(R.drawable.card_list_notif);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tanggal_notif, nama_notif, deskripsi_notif;
        LinearLayout card_view, box_notif;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            tanggal_notif = itemView.findViewById(R.id.tanggal_notif);
            nama_notif = itemView.findViewById(R.id.nama_notif);
            deskripsi_notif = itemView.findViewById(R.id.deskripsi_notif);
            card_view = itemView.findViewById(R.id.card_view);
            box_notif = itemView.findViewById(R.id.box_notif);

        }
    }
}
