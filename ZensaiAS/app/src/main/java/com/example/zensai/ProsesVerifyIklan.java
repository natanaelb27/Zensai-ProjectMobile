package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProsesVerifyIklan extends AppCompatActivity {
    UrlClassHandler urlhandler = new UrlClassHandler();
    private Button btnapproveiklanadmin, btnrefuseiklanadmin;
//    private ImageView imgiklanadmin;
    private TextView textiklanadmin;
    private String getImageIklan, id_iklan, getTextIklan;
    private String urlupdateiklan = urlhandler.hosturl + "updateiklanbyadmin.php";
    private String urlgetimageiklan = urlhandler.hosturl + "selectimageiklanbyadmin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_verify_iklan);
        id_iklan = getIntent().getStringExtra("id_iklan");
        btnapproveiklanadmin = (Button)findViewById(R.id.btnapproveiklanadmin);
        btnrefuseiklanadmin = (Button)findViewById(R.id.btnrefuseiklanadmin);
        textiklanadmin = (TextView)findViewById(R.id.textiklanadmin);
        btnapproveiklanadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateiklan("approved");
            }
        });
        btnrefuseiklanadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateiklan("refused");
            }
        });
        fetchimageiklan();
    }
    private void updateiklan(final String status){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(ProsesVerifyIklan.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateiklan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        finish();
                        Intent i = new Intent(getApplicationContext(), MainMenuAdmin.class);
                        startActivity(i);
                        Toast.makeText(ProsesVerifyIklan.this, "Iklan " + status, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProsesVerifyIklan.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(ProsesVerifyIklan.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_iklan", id_iklan);
                params.put("status", status);
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
    private void fetchimageiklan(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(ProsesVerifyIklan.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetimageiklan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                    JSONObject a = member.getJSONObject(0);
                    getImageIklan = a.getString(urlhandler.TAG_LINK_PIC_IKLAN);
                    getTextIklan = a.getString(urlhandler.TAG_KETERANGAN_IKLAN);
                    new DownloadImageTask((ImageView) findViewById(R.id.imgiklanadmin)).execute(getImageIklan);
                    if(getTextIklan.matches("null")){
                        textiklanadmin.setText("Tidak ada text");
                    }else{
                        textiklanadmin.setText(getTextIklan);
                    }

                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(ProsesVerifyIklan.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_iklan", id_iklan);
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
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}