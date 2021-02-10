package com.example.zensai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends Fragment {

    private String username, nama, alamat, tgllahir, nohp, linkprofilepic, email;
    private Button btneditprofile, btnchangepassword;
    private TextView textusername, textnamalengkap, texttgllahir, textalamatpelanggan, textnomorhp, textemail;
    UrlClassHandler urlhandler = new UrlClassHandler();
    private final String urlfetchdata =  urlhandler.hosturl + "selectprofiledata.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_my_profile,container,false);
    }
    @Override
    public void onStart() {
        super.onStart();
        btneditprofile = (Button)getActivity().findViewById(R.id.btneditprofile);
        btnchangepassword = (Button)getActivity().findViewById(R.id.btnchangepassword);
        textusername = (TextView)getActivity().findViewById(R.id.textusername);
        textnamalengkap = (TextView)getActivity().findViewById(R.id.textnamalengkap);
        texttgllahir = (TextView)getActivity().findViewById(R.id.texttgllahir);
        textalamatpelanggan = (TextView)getActivity().findViewById(R.id.textalamatpelanggan);
        textnomorhp = (TextView)getActivity().findViewById(R.id.textnomorhp);
        textemail = (TextView)getActivity().findViewById(R.id.textemail);
        textusername.setText(username);
        fetchdata();
        btneditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), EditProfile.class);
                i.putExtra("username",username);
                i.putExtra("nama",nama);
                i.putExtra("alamat",alamat);
                i.putExtra("tgllahir",tgllahir);
                i.putExtra("nohp",nohp);
                i.putExtra("linkprofilepic",linkprofilepic);
                i.putExtra("email",email);
                startActivity(i);
            }
        });
        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ChangePassword.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });
    }
    private void fetchdata(){
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlfetchdata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(urlhandler.TAG_USER);
                    JSONObject a = member.getJSONObject(0);
                    nama = a.getString(urlhandler.TAG_NAMA_PELANGGAN);
                    alamat = a.getString(urlhandler.TAG_ALAMAT_PELANGGAN);
                    tgllahir = a.getString(urlhandler.TAG_TANGGAL_LAHIR);
                    email = a.getString(urlhandler.TAG_EMAIL);
                    nohp = a.getString(urlhandler.TAG_NOMOR_HP);
                    linkprofilepic = a.getString(urlhandler.TAG_PROFILE_PICTURE);
                    loadImage();
                    textnamalengkap.setText(nama);
                    textalamatpelanggan.setText(alamat);
                    texttgllahir.setText(tgllahir);
                    textnomorhp.setText(nohp);
                    textemail.setText(email);
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
    private void loadImage(){
        new DownloadImageTask((ImageView) getActivity().findViewById(R.id.imgmyprofile)).execute(linkprofilepic);
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
}