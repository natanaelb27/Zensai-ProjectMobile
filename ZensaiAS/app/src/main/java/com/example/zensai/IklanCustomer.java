package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Locale;
import java.util.Map;

public class IklanCustomer extends AppCompatActivity {
    private ListView lv;
    private ArrayList<HashMap<String, String>> list_iklan;
    private String username, get_id;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
    private DecimalFormat formatter = (DecimalFormat)nf;
    private String urlfetchdata = urlhandler.hosturl + "selectiklancustomer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan_customer);
        username = getIntent().getStringExtra("username");
        list_iklan = new ArrayList<>();
        lv = findViewById(R.id.listviewiklancustomer);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                get_id = ((TextView) view.findViewById(R.id.idiklanlist)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ProsesIklan.class);
                intent.putExtra("username", username);
                intent.putExtra("id_iklan", get_id);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(IklanCustomer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idiklan = a.getString(urlhandler.TAG_ID_IKLAN);
                        String tgl = a.getString(urlhandler.TAG_TGL_PEMBAYARAN);
                        String total = a.getString(urlhandler.TAG_TOTAL_HARGA_PEMBAYARAN);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("idiklan", idiklan);
                        map.put("tgl", tgl);
                        map.put("total", formatter.format(Double.parseDouble(total)));
                        list_iklan.add(map);
                    }

                    String[] from = {"idiklan", "tgl", "total"};
                    int[] to = {R.id.idiklanlist, R.id.tglpembayaraniklanlist, R.id.totalpembayaraniklanlist};
                    ListAdapter adapter = new SimpleAdapter(IklanCustomer.this, list_iklan, R.layout.list_iklan, from, to);
                    lv.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(IklanCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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
}