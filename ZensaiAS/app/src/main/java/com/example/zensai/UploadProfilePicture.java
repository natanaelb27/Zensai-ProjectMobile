package com.example.zensai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadProfilePicture extends AppCompatActivity {
    UrlClassHandler urlhandler = new UrlClassHandler();

    private final String HI =  urlhandler.hosturl + "uploadimage.php";
    private ImageView img;
    private Button selectImg, uploadImg,skipupload;
    private Uri filepath;
    Bitmap bitmap;
    final int IMAGE_REQUEST_CODE = 999;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);


        username = getIntent().getStringExtra("username");

        img = (ImageView) findViewById(R.id.imagview);
        selectImg = (Button) findViewById(R.id.select_img);
        uploadImg = (Button) findViewById(R.id.upload_img);
        skipupload = (Button) findViewById(R.id.skipuploadbtn);

        skipupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainMenuCustomer.class);
                i.putExtra("username",username);
                startActivity(i);
                finish();
            }
        });

        uploadImg.setEnabled(false);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(UploadProfilePicture.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQUEST_CODE);
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, HI, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UploadProfilePicture.this,"Gambar berhasil diupload",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),MainMenuCustomer.class);
                        i.putExtra("username",username);
                        startActivity(i);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadProfilePicture.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parms=new HashMap<String, String>();
                        String imgdata=imgToString(bitmap);
                        parms.put("imageurl",imgdata);
                        parms.put("username",username);
                        return parms;

                    }
                };
                RequestQueue rq= Volley.newRequestQueue(UploadProfilePicture.this);
                rq.add(stringRequest);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==IMAGE_REQUEST_CODE){
            if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(new Intent(Intent.ACTION_PICK));
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_REQUEST_CODE);

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        uploadImg.setEnabled(true);
    }
    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes=byteArrayOutputStream.toByteArray();
        String encodeimg= Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return encodeimg;
    }
}