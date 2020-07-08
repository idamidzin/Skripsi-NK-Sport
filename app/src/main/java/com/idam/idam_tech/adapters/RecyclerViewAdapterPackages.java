package com.idam.idam_tech.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.packages.DetailPackageActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Packages;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterPackages extends RecyclerView.Adapter<RecyclerViewAdapterPackages.MyViewHolder> {

    private Context mContext;
    private List<Packages> mData;

    public RecyclerViewAdapterPackages(Context mContext, List<Packages> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_package,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.nama_package.setText(mData.get(position).getNama());

        final String harga = mData.get(position).getHarga();
        int txt_harga = Integer.parseInt(harga)/1000;
        holder.harga.setText(""+txt_harga);
        final String package_id = mData.get(position).getId();

        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, DetailPackageActivity.class);
                intent.putExtra("package_id", package_id);
                intent.putExtra("firstOpen", "package");
                intent.putExtra("promo_id", "0");
                mContext.startActivity(intent);
            }
        });

        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"cekBuyPackageHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String pesan = jsonObject.getString("messages");
                            Log.d("Messages :", pesan);

                            if (pesan.equals("sukses")){
                                String kondisi = jsonObject.getString("status");
                                if(kondisi.equals("destroy")){
                                    Log.d("Kondisi : ", "Paket Opened DESTROY");
                                    holder.btn_buy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.card_view));
                                    holder.btn_buy.setEnabled(true);
                                }else if(kondisi.equals("on")){
                                    Log.d("Kondisi : ", "Paket Closed ON");
                                    holder.btn_buy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.card_view_hidden));
                                    holder.btn_buy.setEnabled(false);
                                }else if(kondisi.equals("off")){
                                    Log.d("Kondisi : ", "Paket Closed OFF");
                                    holder.btn_buy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.card_view_hidden));
                                    holder.btn_buy.setEnabled(false);
                                }
                            }else if(pesan.equals("gagal")){
                                Log.d("Kondisi :", "NEXT");
                                holder.btn_buy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.card_view));
                                holder.btn_buy.setEnabled(true);
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
                SessionManagement sessionManagement=new SessionManagement(mContext);
                int id = sessionManagement.getSession();
                Map<String, String> params = new HashMap<>();
                params.put("member_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        queue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_package;
        TextView jumlah_hari;
        TextView harga;
        LinearLayout btn_buy;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            nama_package = itemView.findViewById(R.id.nama_package);
            harga = itemView.findViewById(R.id.harga);
            btn_buy = itemView.findViewById(R.id.btn_buy);

        }
    }
}
