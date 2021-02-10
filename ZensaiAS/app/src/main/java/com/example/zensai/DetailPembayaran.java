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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailPembayaran extends AppCompatActivity {
    UrlClassHandler urlhandler = new UrlClassHandler();
    private String id_pembayaran, total, getImageBukti;
    private Button btnapprovecashier, btnrefusecashier;
    TextView lbltotalpembayarancashier;
    private ImageView imgbuktipembayarancashier;
//    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
//    private DecimalFormat formatter = (DecimalFormat)nf;
    private String urlupdatetblpembayaran = urlhandler.hosturl + "updatepembayaran.php";
    private String urlgetimagebukti = urlhandler.hosturl + "selectimagebuktipembayaran.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);
        id_pembayaran = getIntent().getStringExtra("id_pembayaran");
        total = getIntent().getStringExtra("totalpembayaran");
        imgbuktipembayarancashier = (ImageView)findViewById(R.id.imgbuktipembayarancashier);
        lbltotalpembayarancashier = (TextView)findViewById(R.id.lbltotalpembayarancashier);
        lbltotalpembayarancashier.setText("Total Pembayaran: Rp." + total);
        btnapprovecashier = (Button)findViewById(R.id.btnapprovecashier);
        btnrefusecashier = (Button)findViewById(R.id.btnrefusecashier);
        fetchbuktipembayaran();
        btnapprovecashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpsTrustManager.allowAllSSL();
                RequestQueue queue = Volley.newRequestQueue(DetailPembayaran.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatetblpembayaran, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                finish();
                                Intent i = new Intent(getApplicationContext(), MainMenuCashier.class);
                                startActivity(i);
                                Toast.makeText(DetailPembayaran.this, "Pembayaran Approved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailPembayaran.this, "Gagal", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPembayaran.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pembayaran", id_pembayaran);
                        params.put("paymentmethod", "method2");
                        params.put("status", "approved");
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
        });

        btnrefusecashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpsTrustManager.allowAllSSL();
                RequestQueue queue = Volley.newRequestQueue(DetailPembayaran.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatetblpembayaran, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                finish();
                                Intent i = new Intent(getApplicationContext(), MainMenuCashier.class);
                                startActivity(i);
                                Toast.makeText(DetailPembayaran.this, "Pembayaran Refused", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailPembayaran.this, "Gagal", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPembayaran.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pembayaran", id_pembayaran);
                        params.put("paymentmethod", "method2");
                        params.put("status", "refused");
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
        });
    }
    private void fetchbuktipembayaran(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(DetailPembayaran.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetimagebukti, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                    JSONObject a = member.getJSONObject(0);
                    getImageBukti = a.getString(urlhandler.TAG_BUKTI_PEMBAYARAN);
                    new DownloadImageTask((ImageView) findViewById(R.id.imgbuktipembayarancashier)).execute(getImageBukti);
                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(DetailPembayaran.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", id_pembayaran);
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