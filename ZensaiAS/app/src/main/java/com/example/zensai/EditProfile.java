package com.example.zensai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private String username, nama, alamat, tgllahir, nohp, linkprofilepic, email, znama, zemail, zalamat, znotelf, ztanggal, imgdata;
    private Button btnchangepicture, btnchoosedate, btnconfirm, btncancel;
    private EditText txbtanggal, txbnama, txbalamat, txbnomor, txbemail;
    private ImageView imgprofile_edit;
    private Uri filepath;
    Bitmap bitmap;
    private Drawable oldDrawable, newDrawable;
    final int IMAGE_REQUEST_CODE = 999;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private String urlupdateuserlogin = urlhandler.hosturl + "updateuserlogin.php";
    private String urlupdatecstdata = urlhandler.hosturl + "updatecstdata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        username = getIntent().getStringExtra("username");
        nama = getIntent().getStringExtra("nama");
        alamat = getIntent().getStringExtra("alamat");
        tgllahir = getIntent().getStringExtra("tgllahir");
        nohp = getIntent().getStringExtra("nohp");
        linkprofilepic = getIntent().getStringExtra("linkprofilepic");
        email = getIntent().getStringExtra("email");
        txbtanggal = (EditText)findViewById(R.id.txbtanggal_edit);
        txbnama = (EditText)findViewById(R.id.txbnama_edit);
        txbalamat = (EditText)findViewById(R.id.txbalamat_edit);
        txbnomor = (EditText)findViewById(R.id.txbphone_edit);
        txbemail = (EditText)findViewById(R.id.txbemail_edit);
        imgprofile_edit = (ImageView)findViewById(R.id.imgprofile_edit);
        txbtanggal.setText(tgllahir);
        txbnama.setText(nama);
        txbalamat.setText(alamat);
        txbnomor.setText(nohp);
        txbemail.setText(email);
        loadImage();
        btnchangepicture = (Button)findViewById(R.id.btnprofilepic_edit);
        btnconfirm = (Button)findViewById(R.id.btnconfirm_edit);
        btncancel = (Button)findViewById(R.id.btncancel_edit);

        btnchangepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditProfile.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQUEST_CODE);
                newDrawable = imgprofile_edit.getDrawable();
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                znama = txbnama.getText().toString();
                zemail = txbemail.getText().toString();
                zalamat = txbalamat.getText().toString();
                znotelf = txbnomor.getText().toString();
                ztanggal = txbtanggal.getText().toString();

                HttpsTrustManager.allowAllSSL();
                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateuserlogin, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdatecstdata, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            int sukses = jObj.getInt("success");
                                            if (sukses == 1) {
                                                Toast.makeText(EditProfile.this, "Update data Berhasil", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(EditProfile.this, MainMenuCustomer.class);
                                                i.putExtra("username",username);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(EditProfile.this, "Update data gagal periksa informasi pribadi anda", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(EditProfile.this, "silahkanhaha cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                                    } }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        if(newDrawable == oldDrawable){
                                            params.put("username", username);
                                            params.put("namalengkap", znama);
                                            params.put("tgl_lahir", ztanggal);
                                            params.put("alamat", zalamat);
                                            params.put("no_hp", znotelf);
                                            params.put("status", "imgnotchanged");
                                            return params;
                                        }else{
                                            String imgdata=imgToString(bitmap);
                                            params.put("username", username);
                                            params.put("namalengkap", znama);
                                            params.put("tgl_lahir", ztanggal);
                                            params.put("alamat", zalamat);
                                            params.put("no_hp", znotelf);
                                            params.put("imageurl", imgdata);
                                            params.put("status", "imgchanged");
                                            return params;
                                        }

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
                                Toast.makeText(EditProfile.this, "Update gagal", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditProfile.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("email", zemail);
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
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), MainMenuCustomer.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });


        btnchoosedate = (Button)findViewById(R.id.btnpilihtanggal_edit);
        btnchoosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mycalendar = Calendar.getInstance();
                DatePickerDialog pickdate = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
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
    }

    private void loadImage(){
        new DownloadImageTask((ImageView) findViewById(R.id.imgprofile_edit)).execute(linkprofilepic);
        oldDrawable = imgprofile_edit.getDrawable();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==IMAGE_REQUEST_CODE){
            if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(new Intent(Intent.ACTION_PICK));
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_REQUEST_CODE);

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imgprofile_edit.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String imgToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes=byteArrayOutputStream.toByteArray();
        String encodeimg= Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return encodeimg;
    }

}