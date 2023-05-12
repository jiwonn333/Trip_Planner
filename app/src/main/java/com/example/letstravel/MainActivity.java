package com.example.letstravel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.letstravel.login.LoginActivity;
import com.example.letstravel.util.UserPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private View header;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        initLayout();

        header.setOnClickListener(v -> {
            setLoginView();
        });
    }

    private void setLoginView() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void initLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.dl_main_drawer);
        navigationView = findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        if (UserPreference.getKakaoLoginSuccess(context) || UserPreference.getNaverLoginSuccess(context)) {

        } else {
            header = navigationView.getHeaderView(0);
            TextView headerName = header.findViewById(R.id.nav_header_tv_name);
            TextView headerId = header.findViewById(R.id.nav_header_tv_id);

            headerName.setText(getString(R.string.required_login));
            headerId.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.direction:
                Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show();
                break;

            case R.id.subway:
                Toast.makeText(this, "item2 clicked..", Toast.LENGTH_SHORT).show();
                break;

            case R.id.favorite:
                Toast.makeText(this, "item3 clicked..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "item4 clicked..", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng seoul = new LatLng(37.556, 126.97);
        mMap.addMarker(new MarkerOptions().position(seoul).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
    }
}