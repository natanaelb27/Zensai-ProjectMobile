package com.example.zensai;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class RegisterProfilePicture extends AppCompatActivity {

    private ImageView profileimage;
    private String urlphoto = "http://192.168.1.19/zensai/menupic/nigiriseta.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile_picture);

        profileimage = (ImageView)findViewById(R.id.Profile);
        Picasso.get().load(urlphoto).into(profileimage);
    }
}