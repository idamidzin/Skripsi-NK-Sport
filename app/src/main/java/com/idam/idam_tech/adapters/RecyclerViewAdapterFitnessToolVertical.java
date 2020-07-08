package com.idam.idam_tech.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.fitnesstools.AlatFitnessActivity;
import com.idam.idam_tech.activities.fitnesstools.AlatFitnessDetailActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.FitnessTool;

import java.util.List;


public class RecyclerViewAdapterFitnessToolVertical extends RecyclerView.Adapter<RecyclerViewAdapterFitnessToolVertical.MyViewHolder> {

    private Context mContext;
    private List<FitnessTool> mData;

    public RecyclerViewAdapterFitnessToolVertical(Context mContext, List<FitnessTool> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterFitnessToolVertical.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_alat_fitness_vertical,parent,false);

        return new RecyclerViewAdapterFitnessToolVertical.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        link link_link = new link();
        final String BASE_URL = link_link.getURL_IMAGE_ALAT();
        holder.titleAlat.setText(mData.get(position).getNama());
        final String nama = mData.get(position).getNama();
        final String gambar = mData.get(position).getGambar();
        final String deskripsi = mData.get(position).getDeskripsi();
        Glide.with(mContext)
                .asBitmap()
                .load(BASE_URL+mData.get(position).getGambar())
                .into(holder.imageAlat);
        holder.btn_alat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, AlatFitnessDetailActivity.class);
                intent.putExtra("nama", nama);
                intent.putExtra("gambar", gambar);
                intent.putExtra("deskripsi", deskripsi);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleAlat;
        ImageView imageAlat;
        CardView btn_alat;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            titleAlat = itemView.findViewById(R.id.titleAlat);
            imageAlat = itemView.findViewById(R.id.imageAlat);
            btn_alat = itemView.findViewById(R.id.btn_alat);

        }
    }
}
