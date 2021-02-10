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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProsesIklan extends AppCompatActivity {

    private String username, id_iklan, imgdata;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private final String HI =  urlhandler.hosturl + "uploadiklan.php";
    private ImageView img;
    private TextInputLayout editTextIklan;
    private Button selectImg, uploadImg;
    private Uri filepath;
    Bitmap bitmap;
    final int IMAGE_REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_iklan);
        username = getIntent().getStringExtra("username");
        id_iklan = getIntent().getStringExtra("id_iklan");
        img = (ImageView) findViewById(R.id.imageiklan);
        selectImg = (Button) findViewById(R.id.select_imgiklan);
        uploadImg = (Button) findViewById(R.id.upload_imgiklan);
        editTextIklan = (TextInputLayout) findViewById(R.id.editTextIklan);
        uploadImg.setEnabled(false);
        editTextIklan.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextIklan.getEditText().getText().toString().trim().matches("")){
                    uploadImg.setEnabled(false);
                    checkImageView();
                }else{
                    uploadImg.setEnabled(true);
                }

            }
        });
        checkTextIklan();
        checkImageView();
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ProsesIklan.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQUEST_CODE);
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textLength() || img.getDrawable()!=null){
                    if(img.getDrawable()==null){
                        imgdata="kosong";
                    }else{
                        imgdata=imgToString(bitmap);
                    }
                    HttpsTrustManager.allowAllSSL();
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, HI, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ProsesIklan.this,"Iklan berhasil diupload",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),MainMenuCustomer.class);
                            i.putExtra("username", username);
                            startActivity(i);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProsesIklan.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parms=new HashMap<String, String>();
                            parms.put("imageurl",imgdata);
                            parms.put("username",username);
                            parms.put("id_iklan",id_iklan);
                            parms.put("text_iklan", editTextIklan.getEditText().getText().toString().trim());
                            return parms;

                        }
                    };
                    RequestQueue rq= Volley.newRequestQueue(ProsesIklan.this);
                    rq.add(stringRequest);
                }else{

                }
            }
        });
    }

    private void checkImageView(){
        if(img.getDrawable()==null){
            uploadImg.setEnabled(false);
        }else{
            uploadImg.setEnabled(true);
        }
    }
    private void checkTextIklan(){
        if(editTextIklan.getEditText().getText().toString().trim().matches("")){
            uploadImg.setEnabled(false);
            checkImageView();
        }else{
            uploadImg.setEnabled(true);
        }
    }

    private boolean textLength(){
        if(editTextIklan.getEditText().getText().toString().length() > 50) {
            editTextIklan.setError("Text iklan terlalu panjang");
            return false;
        } else {
            return true;
        }
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
        checkImageView();
        checkTextIklan();
    }
    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes=byteArrayOutputStream.toByteArray();
        String encodeimg= Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return encodeimg;
    }
}