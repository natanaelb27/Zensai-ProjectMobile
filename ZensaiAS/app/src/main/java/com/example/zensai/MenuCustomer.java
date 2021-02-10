package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuCustomer extends AppCompatActivity {

    UrlClassHandler urlhandler = new UrlClassHandler();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button btncart;

    private List<DataModelMenu> listitems;

    private String urlfetchdata = urlhandler.hosturl + "selectfoodmenu.php";
    private String urladdtocart = urlhandler.hosturl + "addtocart.php";
    private String urlgetcartquantity = urlhandler.hosturl + "selectcartquantity.php";
    private String username;
    private int getTotalQuantity;
    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    private DecimalFormat formatter = (DecimalFormat)nf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_customer);
        username = getIntent().getStringExtra("username");
        recyclerView = (RecyclerView) findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listitems = new ArrayList<>();
        fetchRecyclerData();
        checkCartQuantity();
        btncart = (Button)findViewById(R.id.btncart);
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CartCustomer.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });


    }

    private void checkCartQuantity(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(MenuCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetcartquantity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        getTotalQuantity = a.getInt(urlhandler.TAG_TOTAL_QUANTITY);
                    }
                    btncart.setText("Cart (" + getTotalQuantity + ")");
                    if(getTotalQuantity == 0){
                        btncart.setEnabled(false);
                    } else {
                        btncart.setEnabled(true);
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
                Toast.makeText(MenuCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
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

    private void fetchRecyclerData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data.....");
        progressDialog.show();

        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONArray jarray = jobj.getJSONArray(urlhandler.TAG_USER);

                    for (int i =0;i<jarray.length();i++){
                        JSONObject jmenu = jarray.getJSONObject(i);
                        DataModelMenu item = new DataModelMenu(
                                jmenu.getString(urlhandler.TAG_NAMA_MENU),
                                jmenu.getString(urlhandler.TAG_DESC_MENU),
                                formatter.format(Double.parseDouble(jmenu.getString(urlhandler.TAG_HARGA_MENU))),
                                jmenu.getString(urlhandler.TAG_GAMBAR_MENU)
                        );

                        listitems.add(item);
                    }

                    adapter = new DataAdapterMenu(listitems, getApplicationContext(), new DataAdapterMenu.ClickListener() {

                        @Override
                        public void onButtonClicked(String nama) {
                            addToCart(nama);
                        }
                    });

                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void addToCart(final String nama_menu){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(MenuCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urladdtocart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        Toast.makeText(MenuCustomer.this, nama_menu + " has been added to the cart!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(MenuCustomer.this, "Silahkan selesaikan orderan terakhir Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MenuCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_menu", nama_menu);
                params.put("username", username);
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