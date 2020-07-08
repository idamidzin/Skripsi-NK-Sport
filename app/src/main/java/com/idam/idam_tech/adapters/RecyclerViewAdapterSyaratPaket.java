package com.idam.idam_tech.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Histori;
import com.idam.idam_tech.models.SyaratPaket;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewAdapterSyaratPaket extends RecyclerView.Adapter<RecyclerViewAdapterSyaratPaket.MyViewHolder> {

    private Context mContext;
    private List<SyaratPaket> mData;

    public RecyclerViewAdapterSyaratPaket(Context mContext, List<SyaratPaket> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSyaratPaket.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_syarat_paket,parent,false);

        return new RecyclerViewAdapterSyaratPaket.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.keterangan.setText(mData.get(position).getKeterangan());
        holder.nomor.setText("-");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomor, keterangan;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            nomor = itemView.findViewById(R.id.nomor);
            keterangan = itemView.findViewById(R.id.keterangan);

        }
    }
}
