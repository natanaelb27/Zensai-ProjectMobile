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
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProsesPembayaran extends AppCompatActivity {
    private String username, totalHarga;
    private double hargaOrder = 0, itungHargaOrder = 0, total;
    private TextView lbltotalharga;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    private DecimalFormat formatter = (DecimalFormat)nf;
    private final String HI =  urlhandler.hosturl + "uploadbuktipembayaran.php";
    private final String urlfetchdata =  urlhandler.hosturl + "selecttotalharga.php";
    private ImageView img;
    private Button selectImg, uploadImg;
    private Uri filepath;
    Bitmap bitmap;
    final int IMAGE_REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_pembayaran);
        username = getIntent().getStringExtra("username");
        lbltotalharga = (TextView)findViewById(R.id.lbltotalharga_pembayaran);
        totalHarga();
//        totalharga = getIntent().getStringExtra("totalharga");
//        try {
//            total = Double.parseDouble(totalHarga);
//        } catch (NumberFormatException nfe) {
//        }
        img = (ImageView) findViewById(R.id.imagebukti);
        selectImg = (Button) findViewById(R.id.select_imgbukti);
        uploadImg = (Button) findViewById(R.id.upload_imgbukti);
        uploadImg.setEnabled(false);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ProsesPembayaran.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQUEST_CODE);
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                StringRequest stringRequest=new StringRequest(Request.Method.POST, HI, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProsesPembayaran.this,"Bukti pembayaran berhasil diupload",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainMenuCustomer.class);
                        i.putExtra("username", username);
                        startActivity(i);
                        finish();

//                        if(total >= 1000000){
//                            Intent i = new Intent(getApplicationContext(), ProsesIklan.class);
//                            i.putExtra("username", username);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            Intent i = new Intent(getApplicationContext(), MainMenuCustomer.class);
//                            i.putExtra("username", username);
//                            startActivity(i);
//                            finish();
//                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProsesPembayaran.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parms=new HashMap<String, String>();
                        String imgdata=imgToString(bitmap);
                        parms.put("imageurl",imgdata);
                        parms.put("username",username);
                        parms.put("totalpembayaran",totalHarga);
                        return parms;

                    }
                };
                RequestQueue rq= Volley.newRequestQueue(ProsesPembayaran.this);
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

    private void totalHarga(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(ProsesPembayaran.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);

                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String totalHargaMenu = a.getString(urlhandler.TAG_TOTAL_HARGA_CART);
                        try {
                            hargaOrder = Double.parseDouble(totalHargaMenu);
                        } catch (NumberFormatException nfe) {

                        }
                        itungHargaOrder = itungHargaOrder + hargaOrder;
                    }
                    totalHarga = String.valueOf(itungHargaOrder);
                    lbltotalharga.setText("Total Pembayaran: Rp. "+formatter.format(Double.parseDouble(totalHarga)));
                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(ProsesPembayaran.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("from", "prosespembayaran");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(stringRequest);
    }




}