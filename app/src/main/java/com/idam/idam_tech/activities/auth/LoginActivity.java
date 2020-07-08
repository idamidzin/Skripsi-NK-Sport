package com.idam.idam_tech.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.idam.idam_tech.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.User;
import com.idam.idam_tech.session.SessionManagement;

public class LoginActivity extends AppCompatActivity {

    link link_link;

    EditText txtUsername, txtPassword;
    TextView link_register;
    Button btn_login;
    ImageView logo;
    private int counterTouchLogo = 0;

//    SessionManager sessionManager;

    public void findViewById(){
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        link_register = (TextView) findViewById(R.id.link_register);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        findViewById();

        Log.d("Token", ""+ FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevice");
//        sessionManager = new SessionManager(this);

        logo = (ImageView) findViewById(R.id.logo);

        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                        counterTouchLogo++;
                        if (counterTouchLogo == 5){
                            startActivity(new Intent(LoginActivity.this, LoginAdminActivity.class));
                        }
                    }catch (Exception e){}
                return false;
            }
        });


        txtUsername.setText(getIntent().getStringExtra("username"));
        txtPassword.setText(getIntent().getStringExtra("password"));

        link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = txtUsername.getText().toString();
                String Password = txtPassword.getText().toString();

                if (!Username.isEmpty() || !Password.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(Username).matches()) {
                        txtUsername.setError("Enter a valid email");
                        txtUsername.requestFocus();
                    }else{
                        Login(Username,Password);
                    }
                }else{
                    txtUsername.setError("Isi Email");
                    txtPassword.setError("Isi Password");
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
        int userID = sessionManagement.getSession();
        if (userID != -1){
            moveToMainActivity();
        }else{
//            Do Nothing Bro
        }
    }

    private void Login(final String txtUsername, final String txtPassword) {
        link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, BASE_URL+"login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");

                            String nik= jsonObject.getString("nik");
                            String username= jsonObject.getString("username");
                            String ids= jsonObject.getString("id");
                            int id=Integer.parseInt(ids);

                            if (messages.equals("sukses")){
                                User user= new User(id, username);
                                SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
                                sessionManagement.saveSession(user);
                                moveToMainActivity();
                            }

                        }catch (Exception error){
                            Toast.makeText(LoginActivity.this, "Email dan Password Tidak Valid", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(i);
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", txtUsername);
                params.put("password", txtPassword);
                params.put("token", FirebaseInstanceId.getInstance().getToken());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }

    private void moveToMainActivity() {
        Intent i=new Intent(LoginActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("firstOpen","home");
        startActivity(i);
    }

}
