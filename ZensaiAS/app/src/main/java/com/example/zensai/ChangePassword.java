package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    private EditText txboldpass, txbnewpass, txbconfirmpass;
    private Button confirm, cancel;
    private String username, oldpass;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private String urlfetchdata = urlhandler.hosturl + "selectpassword.php";
    private String urlchangepassword = urlhandler.hosturl + "updatepassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        username = getIntent().getStringExtra("username");
        txboldpass = (EditText)findViewById(R.id.txboldpassword_changepassword);
        txbnewpass = (EditText)findViewById(R.id.txbnewpassword_changepassword);
        txbconfirmpass = (EditText)findViewById(R.id.txbconfirm_changepassword);
        confirm = (Button)findViewById(R.id.btnconfirm_changepassword);
        cancel = (Button)findViewById(R.id.btncancel_changepassword);
        confirm.setEnabled(false);

        fetchdata();
        checkinput();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChangePassword.this, MainMenuCustomer.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

    }
    private void confirmBtn(final String oldpass){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txboldpass.getText().toString().matches(oldpass)){
                    if(txbnewpass.getText().toString().matches(txbconfirmpass.getText().toString())){
                        HttpsTrustManager.allowAllSSL();
                        RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlchangepassword, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(ChangePassword.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ChangePassword.this, MainMenuCustomer.class);
                                        i.putExtra("username",username);
                                        startActivity(i);
                                        finish();
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
                                Toast.makeText(ChangePassword.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                            } }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("password", txbnewpass.getText().toString());
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
                    }else{
                        Toast.makeText(ChangePassword.this, "'Confirms new password' and 'New password' do not match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePassword.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void fetchdata(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                    JSONObject a = member.getJSONObject(0);
                    oldpass = a.getString(urlhandler.TAG_PASSWORD);
                    confirmBtn(oldpass);
                }
                catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(ChangePassword.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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
    private void checkinput(){
        txboldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkinputtext()){
                    confirm.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                }

            }
        });
        txbnewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkinputtext()){
                    confirm.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                }

            }
        });
        txbconfirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(checkinputtext()){
                    confirm.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                }

            }
        });
    }
    private boolean checkinputtext(){
        if(txboldpass.getText().toString().trim().matches("") || txbnewpass.getText().toString().trim().matches("") || txbconfirmpass.getText().toString().trim().matches("")){
            return false;
        }else{
            return true;
        }
    }

}