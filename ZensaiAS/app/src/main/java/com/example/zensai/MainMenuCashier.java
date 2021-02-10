package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainMenuCashier extends AppCompatActivity {
    private ListView lvmethod1, lvmethod2, lvbukti;
    private ArrayList<HashMap<String, String>> list_pembayaranmethod1, list_pembayaranmethod2, list_bukti;
    private String get_id_pembayaranmethod1, get_id_pembayaranmethod2, get_totalpembayaran, get_id_cartbukti, get_metode;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private Button btnlogoutcashier, btnrefresh_cashier;
    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    private DecimalFormat formatter = (DecimalFormat)nf;
    private String urlupdatetblpembayaran = urlhandler.hosturl + "updatepembayaran.php";
    private String urlupdatetblcart = urlhandler.hosturl + "updatecartdone.php";
    private String urlfetchdata = urlhandler.hosturl + "selectpembayarancashier.php";
    private String urlfetchdata2 = urlhandler.hosturl + "selectcartpaid.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_cashier);
        list_pembayaranmethod1 = new ArrayList<>();
        list_pembayaranmethod2 = new ArrayList<>();
        list_bukti = new ArrayList<>();
        lvmethod1 = findViewById(R.id.listviewcashier_cashdebitkredit);
        lvmethod2 = findViewById(R.id.listviewcashier_transfer);
        lvbukti = findViewById(R.id.listviewcashier_buktiambil);
        btnrefresh_cashier = (Button)findViewById(R.id.btnrefresh_cashier);
        btnlogoutcashier = (Button)findViewById(R.id.btnlogoutcashier);
        btnlogoutcashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });
        btnrefresh_cashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenuCashier.class);
                startActivity(i);
            }
        });
        fetchmethod1();
        fetchmethod2();
        fetchisibukti();
        clickmethod1();
        clickmethod2();
        clickbukti();
    }
    private void clickmethod1(){
        lvmethod1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                get_id_pembayaranmethod1 = ((TextView) view.findViewById(R.id.idpembayarancashierlist)).getText().toString();
                get_metode = ((TextView) view.findViewById(R.id.metodecashierlist)).getText().toString();
                if(get_metode.matches("debit")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuCashier.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.debit_layout, null);
                    builder.setView(customLayout);
                    builder.setTitle("Approve Debit Payment");
                    builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText nomordebit = customLayout.findViewById(R.id.txbdebitnomor_cashier);
                            EditText namabank = customLayout.findViewById(R.id.txbdebitbank_cashier);
                            updatepembayaran(get_id_pembayaranmethod1, "debit", nomordebit.getText().toString(), namabank.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(get_metode.matches("kredit")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuCashier.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.credit_layout, null);
                    builder.setView(customLayout);
                    builder.setTitle("Approve Credit Payment");
                    builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText nomorkredit = customLayout.findViewById(R.id.txbcreditnomor_cashier);
                            updatepembayaran(get_id_pembayaranmethod1, "kredit", nomorkredit.getText().toString(), "");
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(get_metode.matches("cash")){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainMenuCashier.this).create();
                    alertDialog.setTitle("Approve");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Approve Payment",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    updatepembayaran(get_id_pembayaranmethod1, "cash", "", "");
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

            }
        });
    }
    private void updatepembayaran(final String id_pembayaran, final String metode, final String nomor, final String bank){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(MainMenuCashier.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatetblpembayaran, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(MainMenuCashier.this, "Pembayaran Approved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainMenuCashier.this, "Gagal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainMenuCashier.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", id_pembayaran);
                params.put("paymentmethod", "method1");
                params.put("detailmethod", metode);
                params.put("nomor", nomor);
                params.put("bank", bank);
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
    private void clickmethod2(){
        lvmethod2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                get_id_pembayaranmethod2 = ((TextView) view.findViewById(R.id.idpembayarancashierlist)).getText().toString();
                get_totalpembayaran = ((TextView) view.findViewById(R.id.totalpembayarancashierlist)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), DetailPembayaran.class);
                intent.putExtra("id_pembayaran", get_id_pembayaranmethod2);
                intent.putExtra("totalpembayaran", get_totalpembayaran);
                startActivity(intent);
            }
        });
    }
    private void fetchmethod1(){
        RequestQueue queue = Volley.newRequestQueue(MainMenuCashier.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idpembayaran = a.getString(urlhandler.TAG_ID_PEMBAYARAN);
                        String namapelanggan = a.getString(urlhandler.TAG_NAMA_PELANGGAN);
                        String username = a.getString(urlhandler.TAG_USERNAME);
                        String tgl = a.getString(urlhandler.TAG_TGL_PEMBAYARAN);
                        String total = a.getString(urlhandler.TAG_TOTAL_HARGA_PEMBAYARAN);
                        String metode = a.getString(urlhandler.TAG_METODE);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("idpembayaran", idpembayaran);
                        map.put("namapelanggan", namapelanggan);
                        map.put("username", username);
                        map.put("tgl", tgl);
                        map.put("total", formatter.format(Double.parseDouble(total)));
                        map.put("metode", metode);
                        list_pembayaranmethod1.add(map);
                    }

                    String[] from = {"idpembayaran", "namapelanggan", "username", "tgl", "total", "metode"};
                    int[] to = {R.id.idpembayarancashierlist, R.id.namacustomercashierlist, R.id.usernamecashierlist, R.id.tglcashierlist, R.id.totalpembayarancashierlist, R.id.metodecashierlist};
                    ListAdapter adapter = new SimpleAdapter(MainMenuCashier.this, list_pembayaranmethod1, R.layout.list_pembayaran, from, to);
                    lvmethod1.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainMenuCashier.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("paymentmethod", "method1");
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
    private void fetchmethod2(){
        RequestQueue queue = Volley.newRequestQueue(MainMenuCashier.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idpembayaran = a.getString(urlhandler.TAG_ID_PEMBAYARAN);
                        String namapelanggan = a.getString(urlhandler.TAG_NAMA_PELANGGAN);
                        String username = a.getString(urlhandler.TAG_USERNAME);
                        String tgl = a.getString(urlhandler.TAG_TGL_PEMBAYARAN);
                        String total = a.getString(urlhandler.TAG_TOTAL_HARGA_PEMBAYARAN);
                        String metode = a.getString(urlhandler.TAG_METODE);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("idpembayaran", idpembayaran);
                        map.put("namapelanggan", namapelanggan);
                        map.put("username", username);
                        map.put("tgl", tgl);
                        map.put("total", formatter.format(Double.parseDouble(total)));
                        map.put("metode", metode);
                        list_pembayaranmethod2.add(map);
                    }

                    String[] from = {"idpembayaran", "namapelanggan", "username", "tgl", "total", "metode"};
                    int[] to = {R.id.idpembayarancashierlist, R.id.namacustomercashierlist, R.id.usernamecashierlist, R.id.tglcashierlist, R.id.totalpembayarancashierlist, R.id.metodecashierlist};
                    ListAdapter adapter = new SimpleAdapter(MainMenuCashier.this, list_pembayaranmethod2, R.layout.list_pembayaran, from, to);
                    lvmethod2.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainMenuCashier.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("paymentmethod", "method2");
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
    private void fetchisibukti(){
        RequestQueue queue = Volley.newRequestQueue(MainMenuCashier.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idcart = a.getString(urlhandler.TAG_ID_CART);
                        String namapelanggan = a.getString(urlhandler.TAG_NAMA_PELANGGAN);
                        String username = a.getString(urlhandler.TAG_USERNAME);
                        String tgl = a.getString(urlhandler.TAG_TGL_PEMBAYARAN);
                        String service = a.getString(urlhandler.TAG_SERVICE);
                        String namamenu = a.getString(urlhandler.TAG_NAMA_CART);
                        String quantity = a.getString(urlhandler.TAG_QUANTITY_CART);
                        String harga = a.getString(urlhandler.TAG_HARGA_MENU);
                        String subtotal = a.getString(urlhandler.TAG_SUBTOTAL);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("idcart", idcart);
                        map.put("namapelanggan", namapelanggan);
                        map.put("username", username);
                        map.put("tgl", tgl);
                        map.put("service", service);
                        map.put("namamenu", namamenu);
                        map.put("quantity", quantity);
                        map.put("harga", formatter.format(Double.parseDouble(harga)));
                        map.put("subtotal", formatter.format(Double.parseDouble(subtotal)));
                        list_bukti.add(map);
                    }

                    String[] from = {"idcart", "namapelanggan", "username", "tgl", "total", "service", "namamenu", "quantity", "harga", "subtotal"};
                    int[] to = {R.id.idcart_buktiambil, R.id.namacustomer_buktiambil, R.id.username_buktiambil, R.id.tgl_buktiambil, R.id.service_buktiambil, R.id.namamenu_buktiambil, R.id.quantity_buktiambil, R.id.harga_buktiambil, R.id.subtotal_buktiambil};
                    ListAdapter adapter = new SimpleAdapter(MainMenuCashier.this, list_bukti, R.layout.list_buktiambil, from, to);
                    lvbukti.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainMenuCashier.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } });
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void clickbukti(){
        lvbukti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                get_id_cartbukti = ((TextView) view.findViewById(R.id.idcart_buktiambil)).getText().toString();
                AlertDialog alertDialog = new AlertDialog.Builder(MainMenuCashier.this).create();
                alertDialog.setTitle("Konfirmasi Makanan Sudah Diambil");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updatecart(get_id_cartbukti);
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

    private void updatecart(final String id_cart){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(MainMenuCashier.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatetblcart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(MainMenuCashier.this, "Confirmed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainMenuCashier.this, "Gagal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainMenuCashier.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_cart", id_cart);
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