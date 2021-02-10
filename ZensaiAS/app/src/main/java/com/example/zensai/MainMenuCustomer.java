package com.example.zensai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenuCustomer extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_customer);
        username = getIntent().getStringExtra("username");
        final RekomendasiCustomer fraghomeuser = new RekomendasiCustomer();
        fraghomeuser.setArguments(getIntent().getExtras());

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navmenu);
        bottomnav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) navlistener);

        Bundle startbundle = new Bundle();
        startbundle.putString("username",username);
        fraghomeuser.setArguments(startbundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fraghomeuser).commit();






    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedfragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navrecommendation:
                            selectedfragment = new RekomendasiCustomer();
                            Bundle recomendationbundle = new Bundle();
                            recomendationbundle.putString("username",username);
                            selectedfragment.setArguments(recomendationbundle);
                            break;
//                        case R.id.navmenubook:
//                            break;
//                        case R.id.navnotification:
                            //selectedfragment = new WorkshopList();
//                            break;
                        case R.id.navmypage:
                            selectedfragment = new MyProfile();
                            Bundle mypagebundle = new Bundle();
                            mypagebundle.putString("username",username);
                            selectedfragment.setArguments(mypagebundle);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                            selectedfragment).commit();
                    return true;
                }
            };
}