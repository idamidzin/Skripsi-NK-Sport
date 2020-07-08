package com.idam.idam_tech.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.admin.MainAdminActivity;
import com.idam.idam_tech.api.link;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginAdminActivity extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    Button btn_login;
    ImageView logo;
    private int counterTouchLogo = 0;

    public void findVariable(){
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        logo = (ImageView) findViewById(R.id.logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        findVariable();
        moveToLogin();
        loginMember();
    }

    private void loginMember() {
        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    counterTouchLogo++;
                    if (counterTouchLogo == 5){
                        startActivity(new Intent(LoginAdminActivity.this, LoginActivity.class));
                    }
                }catch (Exception e){}
                return false;
            }
        });
    }

    private void moveToLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifikasiLogin();
            }
        });
    }

    public void verifikasiLogin(){
        String Username = txtUsername.getText().toString();
        String Password = txtPassword.getText().toString();

        if (!Username.isEmpty() || !Password.isEmpty()){
            Login(Username,Password);
        }else{
            txtUsername.setError("Isi Email");
            txtPassword.setError("Isi Password");
        }
    }

    private void Login(final String txtUsername, final String txtPassword) {
        link link = new link();
        final String BASE_URL = link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"loginAdmin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");

                            if (messages.equals("sukses")){
                                Toast.makeText(LoginAdminActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                moveToMainAdminActivity();
                            }

                        }catch (Exception error){
                            Toast.makeText(LoginAdminActivity.this, "Email dan Password Tidak Valid", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(LoginAdminActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginAdminActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginAdminActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", txtUsername);
                params.put("password", txtPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }

    private void moveToMainAdminActivity() {
        Intent i=new Intent(LoginAdminActivity.this, MainAdminActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
