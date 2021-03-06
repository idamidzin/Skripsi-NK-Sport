package com.idam.idam_tech.activities.packages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
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
import com.idam.idam_tech.activities.MainActivity;
import com.idam.idam_tech.activities.profile.EditProfileActivity;
import com.idam.idam_tech.api.link;
import com.idam.idam_tech.models.PackageHistory;
import com.idam.idam_tech.session.SessionManagement;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadBuktiActivity extends AppCompatActivity {

    Button chooseFile, btn_backHome;
    private Bitmap bitmap;
    ImageView image_bukti;

    public void findVariable(){
        chooseFile = (Button) findViewById(R.id.chooseFile);
        btn_backHome = (Button) findViewById(R.id.btn_backHome);
        image_bukti = (ImageView) findViewById(R.id.image_bukti);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bukti);
        PackageHistory packageHistory=new PackageHistory(UploadBuktiActivity.this);
        String id= String.valueOf(packageHistory.getSessionHistory());
        Log.d("Session History : ", id);

        findVariable();
        moveToUpload();
        moveToHome();
        setImage();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(UploadBuktiActivity.this,
                "Tidak di ijinkan menekan tombol back di perangkatmu",
                Toast.LENGTH_SHORT)
                .show();
    }

    private void moveToHome() {
        btn_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(UploadBuktiActivity.this, MainActivity.class);
                i.putExtra("firstOpen","home");
                startActivity(i);
                finish();
            }
        });
    }

    private void moveToUpload() {
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUploadPhoto();
            }
        });
    }

    public void setImage(){
        link link = new link();
        final String BASE_IMAGE = link.getURL_IMAGE_BUKTI();
        final String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"getImageBukti",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String strFoto= jsonObject.getString("bukti_pembayaran");
                            new UploadBuktiActivity.getImageformUrl(image_bukti).execute(BASE_IMAGE+strFoto);

                        }catch (Exception error){
                            Toast.makeText(UploadBuktiActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadBuktiActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                PackageHistory packageHistory=new PackageHistory(UploadBuktiActivity.this);
                String id = String.valueOf(packageHistory.getSessionHistory());
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class getImageformUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView image_bukti;
        public getImageformUrl(ImageView image_bukti){
            this.image_bukti = image_bukti;
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
            image_bukti.setImageBitmap(bitmap);
        }
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

    private void setUploadPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
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
                image_bukti.setImageBitmap(bitmap);
            }catch (Exception error){
                Toast.makeText(UploadBuktiActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
            PackageHistory packageHistory=new PackageHistory(UploadBuktiActivity.this);
            String id = String.valueOf(packageHistory.getSessionHistory());
            UploadPicture(id, getStringImage(bitmap),ext);
        }
    }

    private void UploadPicture(final String id, final String photo,final String extension) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        link link = new link();
        final String BASE_URL = link.getBASE_URL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BASE_URL+"uploadBuktiPackageHistory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("hasil response", response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String messages = jsonObject.getString("messages");
                            if (messages.equals("sukses")){
                                Toast.makeText(UploadBuktiActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception error){
                            error.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UploadBuktiActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(UploadBuktiActivity.this, "Jaringan Tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("bukti_pembayaran", photo);
                params.put("ext", extension);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imagByteArray, Base64.DEFAULT);
        return encodeImage;
    }
}
