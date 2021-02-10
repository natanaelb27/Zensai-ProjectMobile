package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartCustomer extends AppCompatActivity {
    UrlClassHandler urlhandler = new UrlClassHandler();
    private String username, totalHarga;
    private TextView lbltotalharga;
    private Button btnordercart;
    private double itungHargaOrder = 0, hargaOrder = 0, getTotalQuantity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<DataModelCart> listitems;
    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    private DecimalFormat formatter = (DecimalFormat)nf;
    private String urlfetchdata = urlhandler.hosturl + "selectcustomercart.php";
    private String urladdtocart = urlhandler.hosturl + "addtocart.php";
    private String urlupdateminus = urlhandler.hosturl + "updateminusquantity.php";
    private String urlgettotalharga = urlhandler.hosturl + "selecttotalharga.php";
    private String urlgetcartquantity = urlhandler.hosturl + "selectcartquantity.php";
    private String urlupdateservice = urlhandler.hosturl + "updateserviceandstatus.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customer);
        username = getIntent().getStringExtra("username");
        recyclerView = (RecyclerView) findViewById(R.id.recyclercart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lbltotalharga = (TextView)findViewById(R.id.lbltotalharga);
        btnordercart = (Button)findViewById(R.id.btnorder_cart);
        listitems = new ArrayList<>();
        fetchRecyclerData();
        totalHarga();
        checkCartQuantity();
        btnordercart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(CartCustomer.this).create();
                alertDialog.setTitle("Service Method");
                alertDialog.setMessage("Dine In atau Take Away?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Dine In",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateServiceAndStatus("dine in");
                                Intent i = new Intent(getApplicationContext(), MainMenuCustomer.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Takeaway",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateServiceAndStatus("takeaway");
                                Intent i = new Intent(getApplicationContext(), MainMenuCustomer.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void updateServiceAndStatus(final String service){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(CartCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateservice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                    } else {
                        Toast.makeText(CartCustomer.this, "Pemilihan Service Gagal!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("service", service);
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

    private void checkCartQuantity(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(CartCustomer.this);
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
                    if(getTotalQuantity == 0){
                        Intent i = new Intent(getApplicationContext(), MenuCustomer.class);
                        i.putExtra("username", username);
                        startActivity(i);
                    } else {
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
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONArray jarray = jobj.getJSONArray(urlhandler.TAG_USER);

                    for (int i=0;i<jarray.length();i++){
                        JSONObject jcart = jarray.getJSONObject(i);
                        DataModelCart item = new DataModelCart(
                                jcart.getString(urlhandler.TAG_NAMA_CART),
                                jcart.getString(urlhandler.TAG_DESC_CART),
                                formatter.format(Double.parseDouble(jcart.getString(urlhandler.TAG_HARGA_CART))),
                                jcart.getString(urlhandler.TAG_GAMBAR_CART),
                                jcart.getInt(urlhandler.TAG_QUANTITY_CART)
                        );
                        listitems.add(item);
                    }

                    adapter = new DataAdapterCart(listitems, getApplicationContext(), new DataAdapterCart.ClickListener() {
                        @Override
                        public void onPlusClicked(String nama) {
                            updatePlus(nama);
                        }

                        @Override
                        public void onMinusClicked(String nama) {
                            updateMinus(nama);
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
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        }) {
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

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void updatePlus(final String nama_menu){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(CartCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urladdtocart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(CartCustomer.this, "1 " + nama_menu + " has been added to the cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CartCustomer.this, "Register data gagal periksa informasi pribadi anda", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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

    private void updateMinus(final String nama_menu){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(CartCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateminus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(CartCustomer.this, "1 " + nama_menu + " has been removed from the cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CartCustomer.this, "Register data gagal periksa informasi pribadi anda", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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

    private void totalHarga(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(CartCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgettotalharga, new Response.Listener<String>() {
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
                    lbltotalharga.setText(formatter.format(Double.parseDouble(totalHarga)));
                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(CartCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("from", "cartcustomer");
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