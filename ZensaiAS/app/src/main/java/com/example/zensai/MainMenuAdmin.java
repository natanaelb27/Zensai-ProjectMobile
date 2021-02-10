package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenuAdmin extends AppCompatActivity {
    private ListView lv;
    private ArrayList<HashMap<String, String>> list_iklan;
    private String get_id;
    private Button btnlogoutadmin, btnrefresh_admin;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private String urlfetchdata = urlhandler.hosturl + "selectiklanbyadmin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_admin);
        list_iklan = new ArrayList<>();
        btnrefresh_admin = (Button)findViewById(R.id.btnrefresh_admin);
        btnlogoutadmin = (Button)findViewById(R.id.btnlogoutadmin);
        btnlogoutadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });
        lv = findViewById(R.id.listviewiklanadmin);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                get_id = ((TextView) view.findViewById(R.id.idiklanadminlist)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ProsesVerifyIklan.class);
                intent.putExtra("id_iklan", get_id);
                startActivity(intent);

            }
        });
        btnrefresh_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenuAdmin.class);
                startActivity(i);
            }
        });
        fetchdata();
    }
    private void fetchdata(){
        RequestQueue queue = Volley.newRequestQueue(MainMenuAdmin.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        String idiklan = a.getString(urlhandler.TAG_ID_IKLAN);
                        String nama = a.getString(urlhandler.TAG_NAMA_PELANGGAN);
                        String username = a.getString(urlhandler.TAG_USERNAME);
                        String tgl = a.getString(urlhandler.TAG_TGL_PEMBAYARAN);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("idiklan", idiklan);
                        map.put("nama", nama);
                        map.put("username", username);
                        map.put("tgl", tgl);
                        list_iklan.add(map);
                    }

                    String[] from = {"idiklan", "nama", "username", "tgl"};
                    int[] to = {R.id.idiklanadminlist, R.id.namacustomeradminlist, R.id.usernamecustomeradminlist, R.id.tanggalpembayaranadminlist};
                    ListAdapter adapter = new SimpleAdapter(MainMenuAdmin.this, list_iklan, R.layout.list_iklan_admin, from, to);
                    lv.setAdapter(adapter);

                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(MainMenuAdmin.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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