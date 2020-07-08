package com.idam.idam_tech.member.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.idam.idam_tech.activities.auth.LoginActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.activities.profile.EditProfileActivity;
import com.idam.idam_tech.R;
import com.idam.idam_tech.models.PackageHistory;
import com.idam.idam_tech.session.SessionManagement;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    TextView txtUsername, txtNik, txtStatusMember, action_bar_title;
    Button btn_editProfile, btn_logout, btn_qrCode, btn_close, btn_upload_bukti, btn_viewPackageHistory;
    RequestOptions option;
    CircleImageView profile_image;

//    Inisialisasi Card
    LinearLayout layout_profile_image_card, mycontent, overbox;
    CircleImageView profile_image_card;
    ImageView qrcode_image;
    Animation formsmall, formnothing, formcircle, togo;
    ScrollView scrollView;


    private Bitmap bitmap;

    public void findVarible(View view){

        txtUsername = (TextView) view.findViewById(R.id.txtUsername);
        txtNik = (TextView) view.findViewById(R.id.txtNik);
        txtStatusMember = (TextView) view.findViewById(R.id.txtStatusMember);
        btn_editProfile = (Button) view.findViewById(R.id.btn_editProfile);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_qrCode = (Button) view.findViewById(R.id.btn_qrCode);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);


        //        Inisial Get Data Card
        btn_close = (Button) view.findViewById(R.id.btn_close);
        layout_profile_image_card = (LinearLayout) view.findViewById(R.id.layout_profile_image_card);
        mycontent = (LinearLayout) view.findViewById(R.id.mycontent);
        overbox = (LinearLayout) view.findViewById(R.id.overbox);
        profile_image_card = (CircleImageView) view.findViewById(R.id.profile_image_card);
        qrcode_image = (ImageView) view.findViewById(R.id.qrcode_image);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        findVarible(view);
        cekBuyPackageHistory();

        // FUNCTION
        moveToEditProfileActivity();
        moveToCardQrCode();
        moveToBackAnim();
        getDetailUser();
        signOut();
        // END FUNCTION

        formsmall = AnimationUtils.loadAnimation(getContext(), R.anim.formsmall);
        formnothing = AnimationUtils.loadAnimation(getContext(), R.anim.formnothing);
        formcircle = AnimationUtils.loadAnimation(getContext(), R.anim.formcircle);
        togo = AnimationUtils.loadAnimation(getContext(), R.anim.togo);
        mycontent.setAlpha(0);
        overbox.setAlpha(0);
        layout_profile_image_card.setVisibility(View.GONE);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView
                Log.d("scrol event : ", String.valueOf(scrollY));
            }
        });

        return view;
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
                            Log.d("messages", pesan);

                            if(pesan.equals("sukses")){
                                String days = jsonObject.getString("days");
                                String status = jsonObject.getString("status");
                                Log.d("Status", status);
                                if(status.equals("off")){
                                    txtStatusMember.setText("Member Off");
                                }else {
                                    txtStatusMember.setText(days+" hari lagi");
                                }
                            }

                            if(pesan.equals("gagal")){
                                txtStatusMember.setText("Member Off");
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


    private void createQrCode(){
        String textQr = txtNik.getText().toString();
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try {
            BitMatrix bitMatrix=multiFormatWriter.encode(textQr, BarcodeFormat.QR_CODE, 1000,1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            final Bitmap bitmap= barcodeEncoder.createBitmap(bitMatrix);

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
                                    String days = jsonObject.getString("days");
                                    String kondisi = jsonObject.getString("status");
                                    if(kondisi.equals("destroy")){
                                        Log.d("Kondisi : ", "DESTROY");
                                        qrcode_image.setImageResource(R.drawable.not_package);
                                    }else if(kondisi.equals("on")){
                                        Log.d("Kondisi : ", "ON");
                                        qrcode_image.setImageBitmap(bitmap);
                                    }else if(kondisi.equals("off")){
                                        Log.d("Kondisi : ", "OFF");
                                        qrcode_image.setImageResource(R.drawable.not_package);
                                    }
                                }else if(pesan.equals("gagal")){
                                    Log.d("Kondisi :", "NEXT");
                                    qrcode_image.setImageResource(R.drawable.not_package);
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
                    SessionManagement sessionManagement=new SessionManagement(getContext());
                    int id = sessionManagement.getSession();
                    Map<String, String> params = new HashMap<>();
                    params.put("member_id", String.valueOf(id));
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(stringRequest);

        }catch (Exception e){}
    }

    private void moveToBackAnim() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                overbox.startAnimation(togo);
                mycontent.startAnimation(togo);
                layout_profile_image_card.startAnimation(togo);
                layout_profile_image_card.setVisibility(View.GONE);

                ViewCompat.animate(mycontent).setStartDelay(1000).alpha(0).start();
                ViewCompat.animate(overbox).setStartDelay(1000).alpha(0).start();
            }
        });
    }

    private void moveToCardQrCode() {
        btn_qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQrCode();
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                mycontent.setAlpha(1);
                mycontent.startAnimation(formsmall);
                overbox.setAlpha(1);
                overbox.startAnimation(formnothing);
                layout_profile_image_card.setVisibility(View.VISIBLE);
                layout_profile_image_card.startAnimation(formcircle);
            }
        });
    }

    private void moveToEditProfileActivity() {
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signOut(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement=new SessionManagement(getContext());
                sessionManagement.removeSession();
                PackageHistory packageHistory=new PackageHistory(getContext());
                packageHistory.removeSessionHistory();
                moveToLogin();
            }
        });
    }

    public class getImageformUrl extends AsyncTask<String, Void, Bitmap>{
        CircleImageView profile_image;
        public getImageformUrl(CircleImageView profile_image){
            this.profile_image = profile_image;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            profile_image.setImageBitmap(bitmap);
        }
    }

    public void getDetailUser(){
        link link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        final String URL_IMAGE_MEMBER = link_link.getURL_IMAGE();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"members",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            Log.d("tes", jsonObject.getString("foto"));
                            txtUsername.setText(jsonObject.getString("nama_lengkap"));
                            txtNik.setText(jsonObject.getString("nik"));
                            String strFoto= jsonObject.getString("foto");

                            new getImageformUrl(profile_image).execute(URL_IMAGE_MEMBER+strFoto);
                            new getImageformUrl(profile_image_card).execute(URL_IMAGE_MEMBER+strFoto);

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

    private void moveToLogin() {
        Intent i=new Intent(getContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("firstOpen","home");
        startActivity(i);
    }


}
