package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    UrlClassHandler urlhandler = new UrlClassHandler();

    private TextView lblregister;
    private EditText edtusername,edtpassword;
    private String username,password;
    private String urlcheckdata = urlhandler.hosturl + "login.php";

    private String LA,Uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button btnlogin = (Button)findViewById(R.id.btnlogin);
        edtusername = (EditText) findViewById(R.id.txbusername);
        edtpassword = (EditText) findViewById(R.id.txbpassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtusername.getText().toString();
                password = edtpassword.getText().toString();
                if(username.trim().matches("") && password.trim().matches("")){
                    Toast.makeText(LoginPage.this,"Mohon isi username dan password sebelum login",Toast.LENGTH_LONG).show();
                }else if(username.trim().matches("")){
                    Toast.makeText(LoginPage.this,"Mohon isi dulu untuk kolom username",Toast.LENGTH_LONG).show();
                }else if(password.trim().matches("")){
                    Toast.makeText(LoginPage.this,"Mohon isi dulu untuk kolom password",Toast.LENGTH_LONG).show();
                }else if(password.trim().contains("'")){
                    Toast.makeText(LoginPage.this,"Username atau password salah! Silahkan ulangi dengan data yang benar",Toast.LENGTH_LONG).show();
                }else{
                    HttpsTrustManager.allowAllSSL();
                    RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcheckdata, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                int sukses = jObj.getInt("success");
                                if (sukses == 1) {
                                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                                    JSONObject a=member.getJSONObject(0);
                                    LA=a.getString(urlhandler.TAG_STATUS);
                                    Uname = a.getString(urlhandler.TAG_USERNAME);
                                    //Toast.makeText(Loginpage.this, "status"+LA, Toast.LENGTH_SHORT).show();

                                    if(LA.equals("admin")){
                                        Intent i = new Intent(getApplicationContext(), MainMenuAdmin.class);
                                        i.putExtra("username",Uname);
                                        startActivity(i);
                                        finish();
                                        Toast.makeText(LoginPage.this,"selamat datang admin",Toast.LENGTH_LONG).show();
                                    }
                                    if(LA.equals("customer")){
                                        Intent i = new Intent(getApplicationContext(),MainMenuCustomer.class);
                                        i.putExtra("username",Uname);
                                        startActivity(i);
                                        finish();
                                        //Toast.makeText(LoginPage.this,"selamat datang customer",Toast.LENGTH_LONG).show();
                                    }
                                    if(LA.equals("kitchen")){
                                        Intent i = new Intent(getApplicationContext(), MainMenuKitchen.class);
                                        startActivity(i);
                                        finish();
                                        //Toast.makeText(LoginPage.this,"selamat datang customer",Toast.LENGTH_LONG).show();
                                    }
                                    if(LA.equals("cashier")){
                                        Intent i = new Intent(getApplicationContext(), MainMenuCashier.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    //finish();
                                } else {
                                    Toast.makeText(LoginPage.this, "Username atau Password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                        } }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
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
        });

        lblregister = (TextView)findViewById(R.id.lblregister);
        lblregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this,RegisterCustomer.class);
                startActivity(i);
            }
        });
    }
}
