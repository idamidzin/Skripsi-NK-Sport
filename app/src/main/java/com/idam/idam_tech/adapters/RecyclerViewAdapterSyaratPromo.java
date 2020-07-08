package com.idam.idam_tech.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idam.idam_tech.R;
import com.idam.idam_tech.models.SyaratPaket;
import com.idam.idam_tech.models.SyaratPromo;

import java.util.List;


public class RecyclerViewAdapterSyaratPromo extends RecyclerView.Adapter<RecyclerViewAdapterSyaratPromo.MyViewHolder> {

    private Context mContext;
    private List<SyaratPromo> mData;

    public RecyclerViewAdapterSyaratPromo(Context mContext, List<SyaratPromo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSyaratPromo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_syarat_promo,parent,false);

        return new RecyclerViewAdapterSyaratPromo.MyViewHolder(view);
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
