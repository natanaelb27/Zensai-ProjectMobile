package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterCustomer extends AppCompatActivity {

    UrlClassHandler urlhandler = new UrlClassHandler();

    private Button btnchoosedate, btnconfirm;
    private EditText txbnama,txbemail, txbalamat, txbnotelefon, txbusername, txbpassword, txbtanggal;

    private String znama,zemail,zalamat,znotelf,zusername,zpassword,ztanggal;
    private String urlregistercstdata =urlhandler.hosturl + "registercstdata.php";
    private String urlregisteruserlogin = urlhandler.hosturl + "registeruserlogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        txbtanggal = (EditText)findViewById(R.id.txbtanggal);
        txbnama = (EditText)findViewById(R.id.txbname);
        txbemail = (EditText)findViewById(R.id.txbemail);
        txbalamat = (EditText)findViewById(R.id.txbalamat);
        txbnotelefon = (EditText)findViewById(R.id.txbnotelp);
        txbusername = (EditText)findViewById(R.id.txbusername);
        txbpassword = (EditText)findViewById(R.id.txbpassword);

        btnchoosedate = (Button)findViewById(R.id.btnpilihtanggal);
        btnchoosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mycalendar = Calendar.getInstance();
                DatePickerDialog pickdate = new DatePickerDialog(RegisterCustomer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,month,dayOfMonth);
                        txbtanggal.setText(sdf.format(newDate.getTime()));
                    }
                }, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                pickdate.show();
            }
        });

        btnconfirm = (Button)findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpsTrustManager.allowAllSSL();

                znama = txbnama.getText().toString();
                zemail = txbemail.getText().toString();
                zalamat = txbalamat.getText().toString();
                znotelf = txbnotelefon.getText().toString();
                zusername = txbusername.getText().toString();
                zpassword = txbpassword.getText().toString();
                ztanggal = txbtanggal.getText().toString();

                if(checkUsername(zusername) && checkPassword(zpassword)){
                    RequestQueue queue = Volley.newRequestQueue(RegisterCustomer.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregisteruserlogin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                int sukses = jObj.getInt("success");
                                if (sukses == 1) {
                                    RequestQueue queue = Volley.newRequestQueue(RegisterCustomer.this);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregistercstdata, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                int sukses = jObj.getInt("success");
                                                if (sukses == 1) {
                                                    Toast.makeText(RegisterCustomer.this, "Register data Berhasil", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(RegisterCustomer.this,UploadProfilePicture.class);
                                                    i.putExtra("username",zusername);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterCustomer.this, "Register data gagal periksa informasi pribadi anda", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(RegisterCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                                        } }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("username", zusername);
                                            params.put("namalengkap", znama);
                                            params.put("tgl_lahir", ztanggal);
                                            params.put("alamat", zalamat);
                                            params.put("no_hp", znotelf);
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
                                } else {
                                    Toast.makeText(RegisterCustomer.this, "Register data gagal mohon periksa username, email dan password anda", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RegisterCustomer.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                        } }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", zusername);
                            params.put("password", zpassword);
                            params.put("email", zemail);
                            params.put("status", "customer");
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
                    if(zusername.matches("") && zpassword.matches("")){
                        Toast.makeText(RegisterCustomer.this, "Silahkan isi username dan password terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else if(zusername.matches("")){
                        Toast.makeText(RegisterCustomer.this, "Silahkan isi username terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else if(zpassword.matches("")){
                        Toast.makeText(RegisterCustomer.this, "Silahkan isi password terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean checkPassword(String password){
        if(password.matches("")){
            return false;
        }else{
            if(password.contains("'")){
                Toast.makeText(RegisterCustomer.this, "Silahkan isi password tanpa tanda petik", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                return true;
            }
        }
    }
    private boolean checkUsername(String username){
        if(username.matches("")){
            return false;
        }else{
            if(username.contains("'")){
                Toast.makeText(RegisterCustomer.this, "Silahkan isi username tanpa tanda petik", Toast.LENGTH_SHORT).show();
                return false;
        }else{
            return true;
        }
        }
    }
}