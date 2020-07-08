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

import com.idam.idam_tech.activities.promos.DetailPromoActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Promo;
import java.util.List;


public class RecyclerViewAdapterPromoHorizontal extends RecyclerView.Adapter<RecyclerViewAdapterPromoHorizontal.MyViewHolder> {

    private Context mContext;
    private List<Promo> mData;

    public RecyclerViewAdapterPromoHorizontal(Context mContext, List<Promo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterPromoHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_promo_horizontal,parent,false);

        return new RecyclerViewAdapterPromoHorizontal.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPromoHorizontal.MyViewHolder holder, int position) {
        link link_link = new link();
        final String BASE_URL = link_link.getURL_IMAGE_PROMO();
        holder.titlePromo.setText(mData.get(position).getNama());
        final String promo_id = mData.get(position).getId();
        final String kode_promo = mData.get(position).getKode_promo();
        Glide.with(mContext)
                .asBitmap()
                .load(BASE_URL+mData.get(position).getGambar())
                .into(holder.imagePromo);
        holder.btn_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, DetailPromoActivity.class);
                intent.putExtra("firstOpen","home");
                intent.putExtra("promo_id", promo_id);
                intent.putExtra("kode_promo", kode_promo);
                intent.putExtra("package_id", "0");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titlePromo;
        ImageView imagePromo;
        CardView btn_promo;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            titlePromo = itemView.findViewById(R.id.titlePromo);
            imagePromo = itemView.findViewById(R.id.imagePromo);
            btn_promo = itemView.findViewById(R.id.btn_promo);

        }
    }
}
