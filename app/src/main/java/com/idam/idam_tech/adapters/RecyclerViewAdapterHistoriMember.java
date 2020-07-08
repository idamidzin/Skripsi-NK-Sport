package com.idam.idam_tech.adapters;


import android.content.Context;
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
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.histori.HistoriActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.Histori;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


public class RecyclerViewAdapterHistoriMember extends RecyclerView.Adapter<RecyclerViewAdapterHistoriMember.MyViewHolder> {

    private Context mContext;
    private List<Histori> mData;

    public RecyclerViewAdapterHistoriMember(Context mContext, List<Histori> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterHistoriMember.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_histori_member,parent,false);

        return new RecyclerViewAdapterHistoriMember.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (mData.get(position).getTanggal_mulai() == "null"){
            holder.tanggal_histori.setText("Belum di setujui");
        }else{
            holder.tanggal_histori.setText(mData.get(position).getTanggal_mulai());
        }

        holder.status_histori.setText(mData.get(position).getStatus_history());


        final String package_id = mData.get(position).getPackage_id();

            link link_link = new link();
            final String BASE_URL = link_link.getBASE_URL();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"getNamaPackage",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);

                                Log.d("Data : ",jsonObject.getString("nama"));
                                holder.nama_histori.setText("Pembelian Paket "+jsonObject.getString("nama"));

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
                    params.put("id", String.valueOf(package_id));
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(stringRequest);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tanggal_histori, nama_histori, status_histori;
        LinearLayout box_histori;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            tanggal_histori = itemView.findViewById(R.id.tanggal_histori);
            nama_histori = itemView.findViewById(R.id.nama_histori);
            status_histori = itemView.findViewById(R.id.status_histori);
            box_histori = itemView.findViewById(R.id.box_histori);

        }
    }
}
