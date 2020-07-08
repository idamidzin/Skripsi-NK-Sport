package com.idam.idam_tech.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.packages.PackageActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    link link_link;

    Button btn_photo_upload;
    private Bitmap bitmap;
    CircleImageView profile_image;
    RequestOptions option;
    String URL_IMAGE ="http://192.168.100.21/idam_tech/nksport2020/public/uploads/fotos/";

    private static final String TAG= EditProfileActivity.class.getSimpleName();
    EditText nik,nama,username,password;
    private Menu action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nik = (EditText) findViewById(R.id.txtNik);
        nama = (EditText) findViewById(R.id.txtNama);
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        btn_photo_upload = (Button) findViewById(R.id.btn_photo_upload);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        //Request GLIDE
        option = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_fitness_center_black_24dp)
                .error(R.drawable.ic_fitness_center_black_24dp);

        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUploadPhoto();
            }
        });

        setImage();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(EditProfileActivity.this,
                "Tidak di ijinkan menekan tombol back di perangkatmu",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void setImage(){
        link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        final String BASE_IMAGE = link_link.getURL_IMAGE();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"getImageMember",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");
                            String strFoto= jsonObject.getString("foto");
                            new getImageformUrl(profile_image).execute(BASE_IMAGE+strFoto);
//                            Glide.with(EditProfileActivity.this).load(BASE_IMAGE+strFoto).apply(option).into(profile_image);

                        }catch (Exception error){
                            Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                    }
                })
             {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(EditProfileActivity.this);
                String id = String.valueOf(sessionManagement.getSession());
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    private void setUploadPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            Uri filePath = data.getData();
            String filename = getFileName(filePath);
            String ext = filename.substring(filename.indexOf(".")+1);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                profile_image.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL("http://192.168.100.21/idam_tech/nksport2020/public/uploads/fotos/"+bitmap).getContent()));
                profile_image.setImageBitmap(bitmap);
            }catch (Exception error){
                error.printStackTrace();
            }
            SessionManagement sessionManagement=new SessionManagement(EditProfileActivity.this);
            String id = String.valueOf(sessionManagement.getSession());

            UploadPicture(id, getStringImage(bitmap),ext);
        }
    }

    private void UploadPicture(final String id, final String photo,final String extension) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"uploadImage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("hasil response", response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");
                            if (messages.equals("sukses")){
                                Toast.makeText(EditProfileActivity.this, "Sukses", Toast.LENGTH_SHORT).show();

                            }
                        }catch (Exception error){
                            error.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("foto", photo);
                params.put("ext", extension);
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagByteArray, Base64.DEFAULT);

        return encodeImage;
    }

    //    Menampilkan User Detail
    private void getUserDetail(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"memberDetail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (messages.equals("sukses")){
//                                Toast.makeText(EditProfileActivity.this, "Sukses ", Toast.LENGTH_SHORT).show();

                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object= jsonArray.getJSONObject(i);
                                    String strNik = object.getString("nik").trim();
                                    String strNama = object.getString("nama_lengkap").trim();
                                    String strUsername = object.getString("username").trim();
                                    String strPassword = object.getString("password").trim();
                                    nik.setText(strNik);
                                    nama.setText(strNama);
                                    username.setText(strUsername);
                                    password.setText(strPassword);
                                }
                            }

                        }catch (Exception error){
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(EditProfileActivity.this);
                String id = String.valueOf(sessionManagement.getSession());
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_edit:
                nama.setFocusableInTouchMode(true);
                nik.setFocusableInTouchMode(true);

                InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nama, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                nama.setFocusableInTouchMode(false);
                nik.setFocusableInTouchMode(false);
                nama.setFocusable(false);
                nik.setFocusable(false);

                return true;

            default:
                Intent i=new Intent(this, MainActivity.class);
                i.putExtra("firstOpen","profile");
                startActivity(i);
                finish();
                return true;

        }
    }

//    Save
    private void SaveEditDetail() {
        final String nik = this.nik.getText().toString().trim();
        final String nama = this.nama.getText().toString().trim();
        final String user = this.username.getText().toString().trim();
        final String pass = this.password.getText().toString().trim();

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        link_link = new link();
        final String BASE_URL = link_link.getBASE_URL();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, BASE_URL+"editMemberDetail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String messages = jsonObject.getString("messages");

                            if (messages.equals("sukses")){
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Sukses Update !", Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(EditProfileActivity.this, EditProfileActivity.class);
                                i.putExtra("openFirst","profile");
                                startActivity(i);
                                finish();
                            }

                        }catch (Exception error){
                            Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManagement sessionManagement=new SessionManagement(EditProfileActivity.this);
                String id = String.valueOf(sessionManagement.getSession());
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("username", user);
                params.put("nama_lengkap", nama);
                params.put("password", pass);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
