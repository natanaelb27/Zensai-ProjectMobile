package com.example.zensai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainMenuKitchen extends AppCompatActivity {
    private ListView lv;
    private ArrayList<HashMap<String, String>> list_order;
    private String get_id;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private Button btnlogoutkitchen, btnrefresh_kitchen;
    private String urlfetchdata = urlhandler.hosturl + "selectcartkitchen.php";
    private String urlapproveorder = urlhandler.hosturl + "approveorder.php";
    private String urltolakorder = urlhandler.hosturl + "tolakorder.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_kitchen);
        btnrefresh_kitchen = (Button)findViewById(R.id.btnrefresh_kitchen);
        btnlogoutkitchen = (Button)findViewById(R.id.btnlogoutkitchen);
        btnlogoutkitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);
            }
        });
        btnrefresh_kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenuKitchen.class);
                startActivity(i);
            }
        });
        list_order = new ArrayList<>();
        lv = findViewById(R.id.listviewkitchen);
        RequestQueue queue = Volley.newRequestQueue(MainMenuKitchen.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idcart = a.getString(urlhandler.TAG_ID_CART);
                        String username = a.getString(urlhandler.TAG_USERNAME);
                        String namamenu = a.getString(urlhandler.TAG_NAMA_CART);
                        String quantity = a.getString(urlhandler.TAG_QUANTITY_CART);
                        String service = a.getString(urlhandler.TAG_SERVICE);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("idcart", idcart);
                        map.put("username", username);
                        map.put("namamenu", namamenu);
                        map.put("quantity", quantity);
                        map.put("service", service);
                        list_order.add(map);
                    }

                    String[] from = {"idcart", "username", "namamenu", "quantity", "service"};
                    int[] to = {R.id.idcartkitchenlist, R.id.usernamecustomerkitchenlist, R.id.namamenukitchenlist, R.id.quantitykitchenlist, R.id.servicekitchenlist};
                    ListAdapter adapter = new SimpleAdapter(MainMenuKitchen.this, list_order, R.layout.list_order_kitchen, from, to);
                    lv.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainMenuKitchen.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        queue.add(stringRequest);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                get_id = ((TextView) view.findViewById(R.id.idcartkitchenlist)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuKitchen.this);
                builder.setTitle("Konfirmasi Pesanan");
                builder.setMessage("Approve atau tolak pesanan ini?");
                builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(MainMenuKitchen.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlapproveorder, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    finish();
                                    startActivity(getIntent());
                                } catch (Exception ex) {
                                    Log.e("Error", ex.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error", error.getMessage());
                                Toast.makeText(MainMenuKitchen.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id_cart", get_id);
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
                builder.setNegativeButton("Tolak Pesanan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(MainMenuKitchen.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urltolakorder, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    finish();
                                    startActivity(getIntent());
                                } catch (Exception ex) {
                                    Log.e("Error", ex.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error", error.getMessage());
                                Toast.makeText(MainMenuKitchen.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id_cart", get_id);
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
                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });

    }
}