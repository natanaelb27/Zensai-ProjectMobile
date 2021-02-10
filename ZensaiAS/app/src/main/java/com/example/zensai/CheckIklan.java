package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckIklan extends AppCompatActivity {

    private String username;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private final String urlfetchdata =  urlhandler.hosturl + "selectlistiklan.php";
    private ListView lv;
    private ArrayList<IklanList> list_iklan;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_iklan);
        username = getIntent().getStringExtra("username");
        list_iklan = new ArrayList<>();
        lv = findViewById(R.id.listviewcheckiklan);
        getData();
    }

    private void getData(){
        RequestQueue queue = Volley.newRequestQueue(CheckIklan.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject a = member.getJSONObject(i);
                        IklanList il = new IklanList(
                                a.getString(urlhandler.TAG_LINK_PIC_IKLAN),
                                a.getString(urlhandler.TAG_KETERANGAN_IKLAN));
                        list_iklan.add(il);
                    }
                    adapter=new CustomListAdapter(getApplicationContext(),R.layout.list_check_iklan, list_iklan);
                    lv.setAdapter(adapter);


                } catch (Exception ex) {
                    Log.e("Error", ex.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(CheckIklan.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        queue.add(stringRequest);
//
//        RequestQueue queue = Volley.newRequestQueue(CheckIklan.this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i=0; i<response.length(); i++){
//                    try {
//                        JSONObject jObj = new JSONObject(response);
//                        JSONArray member = jObj.getJSONArray(urlhandler.TAG_USER);
//                        for(int i = 0; i < member.length(); i++) {
//                            JSONObject a = member.getJSONObject(i);
//                            IklanList il=new IklanList(
//                                    a.getString(urlhandler.TAG_LINK_PIC_IKLAN),
//                                    a.getString(urlhandler.TAG_KETERANGAN_IKLAN));
//                            list_iklan.add(il);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                setupData(list_iklan);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue= Volley.newRequestQueue(this);
//        requestQueue.add(request);
    }



}