package com.idam.idam_tech.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.idam.idam_tech.R;
import com.idam.idam_tech.api.link;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
//    private String USER = "", PASS="";
    EditText nik, nama_lengkap, email, password, no_wa;
    TextView link_login;
    Button btn_register;
    ProgressBar loading;
    link link_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nik= (EditText) findViewById(R.id.nik);
        nama_lengkap= (EditText) findViewById(R.id.nama_lengkap);
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        no_wa= (EditText) findViewById(R.id.no_wa);
        loading= (ProgressBar) findViewById(R.id.loading);
        btn_register = (Button) findViewById(R.id.btn_register);
        link_login = (TextView) findViewById(R.id.link_login);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
            }
        });
    }

    private void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);

        final String nik = this.nik.getText().toString().trim();
        final String nama_lengkap = this.nama_lengkap.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String no_wa = this.no_wa.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Enter a valid email");
            this.email.requestFocus();
            loading.setVisibility(View.GONE);
            btn_register.setVisibility(View.VISIBLE);
        }else if (nik.length() < 16 || nik.length() > 16) {
            this.nik.setError("Jumlah nik tidak sesuai format nik");
            this.nik.requestFocus();
            loading.setVisibility(View.GONE);
            btn_register.setVisibility(View.VISIBLE);
        }else if(!email.isEmpty() || !password.isEmpty() || !nama_lengkap.isEmpty() || !password.isEmpty() || !no_wa.isEmpty()){
            link_link = new link();
            final String BASE_URL = link_link.getBASE_URL();
            StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"member",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String messages = jsonObject.getString("messages");
                                String email = jsonObject.getString("username");
                                String pass = jsonObject.getString("password");

                                if (messages.equals("sukses")){
                                    Toast.makeText(RegisterActivity.this, "Register Sukses", Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", email);
                                    bundle.putString("password", password);
                                    Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                                    i.putExtras(bundle);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception Error){
                                Error.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                btn_register.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", FirebaseInstanceId.getInstance().getToken());
                    params.put("nik", nik);
                    params.put("nama_lengkap", nama_lengkap);
                    params.put("username", email);
                    params.put("password", password);
                    params.put("no_whatsapp", no_wa);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(StringRequest);
        }else{
            this.email.setError("Isi Email");
            this.nik.setError("Isi Email");
            this.nama_lengkap.setError("Isi Email");
            this.password.setError("Isi Email");
            this.no_wa.setError("Isi Email");
            loading.setVisibility(View.GONE);
            btn_register.setVisibility(View.VISIBLE);
        }
    }
}
