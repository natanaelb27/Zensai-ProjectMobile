package com.example.zensai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class RekomendasiCustomer extends Fragment {

    private String username;
    private TextView lblusername;
    private Button btnfoodmenu, btnpendingorder, btnlogout, btniklan;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private final String insertpembayaran =  urlhandler.hosturl + "insertpembayaran.php";
    private final String urlfetchdata =  urlhandler.hosturl + "checkcart.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_rekomendasi_customer,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lblusername = (TextView)getActivity().findViewById(R.id.txtusername);
        btnfoodmenu = (Button)getActivity().findViewById(R.id.btnfoodmenu);
        btnpendingorder = (Button)getActivity().findViewById(R.id.btnpendingorder);
        btniklan = (Button)getActivity().findViewById(R.id.btniklan_mainmenu);
        btnlogout = (Button)getActivity().findViewById(R.id.btnlogout);
        btnpendingorder.setEnabled(false);

        checkCart();

        btniklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Action");
                String[] paymentmethod = {"Upload Iklan", "Cek Iklan"};
                builder.setItems(paymentmethod, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Upload Iklan
                                Intent i = new Intent(getContext(),IklanCustomer.class);
                                i.putExtra("username",username);
                                startActivity(i);
                                break;
                            case 1: // Check Iklan
                                Intent i2 = new Intent(getContext(),CheckIklan.class);
                                i2.putExtra("username",username);
                                startActivity(i2);
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnfoodmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),MenuCustomer.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginPage.class);
                startActivity(i);
            }
        });
        btnpendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Payment Method");
                String[] paymentmethod = {"cash", "debit", "kredit", "transfer"};
                builder.setItems(paymentmethod, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // cash
                                insertpembayaran("cash");
                                refresh();
                                break;
                            case 1: // debit
                                insertpembayaran("debit");
                                refresh();
                                break;
                            case 2: // kredit
                                insertpembayaran("kredit");
                                refresh();
                                break;
                            case 3: //transfer
                                Intent i = new Intent(getContext(), ProsesPembayaran.class);
                                i.putExtra("username",username);
                                startActivity(i);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        lblusername.setText(username);

    }
    private void refresh(){
        Intent i = new Intent(getContext(), MainMenuCustomer.class);
        i.putExtra("username", username);
        startActivity(i);
    }
    private void insertpembayaran(final String paymentmethod){
        HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, insertpembayaran, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parms=new HashMap<String, String>();
                parms.put("username",username);
                parms.put("paymentmethod",paymentmethod);
                return parms;

            }
        };
        RequestQueue rq= Volley.newRequestQueue(getContext());
        rq.add(stringRequest);

    }


    private void checkCart(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        btnpendingorder.setEnabled(true);

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
                Toast.makeText(getActivity(), "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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