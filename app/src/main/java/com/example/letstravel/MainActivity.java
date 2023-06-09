package com.example.letstravel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.letstravel.fragment.information.InformationViewModel;
import com.example.letstravel.util.PermissionUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * 위치 권한 요청 요청 코드
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * {@link#onRequestPermissionsResult(int, String[], int[])}에서 반환된 후
     * 요청된 권한이 거부되었는지 여부를 나타내는 플래그
     */
    private boolean permissionDenied = false;

    private GoogleMap googleMap;
    private Marker currentMarker = null;
    private InformationViewModel informationViewModel;
    private Geocoder geocoder;
    private List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    public void initView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);

        // bottom navigation 탐색 설정
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        setDefaultLocation();
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        enableMyLocation();


        geocoder = new Geocoder(this);
        informationViewModel = new ViewModelProvider(this).get(InformationViewModel.class);
        informationViewModel.getSearchWord().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Log.e("test", "s : " + s);
                        try {
                            addressList = geocoder.getFromLocationName(s, 1);
                            String []splitStr = addressList.get(0).toString().split(",");
                            double lat = Double.parseDouble(splitStr[10].substring(splitStr[10].indexOf("=") + 1));
                            double lng = Double.parseDouble(splitStr[12].substring(splitStr[12].indexOf("=") + 1));
                            String title = splitStr[1].substring(splitStr[1].indexOf("=") + 1);

                            setLocation(lat, lng, title);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        // 콤마를 기준으로 split

//                        Log.e("test", "splitStr : " + Arrays.toString(splitStr));
//                        Log.e("test", "위도 : " + splitStr[10].substring(splitStr[10].indexOf("=") + 1));
//                        Log.e("test", "경도 : " + splitStr[12].substring(splitStr[12].indexOf("=") + 1));


//                        markerOptions.title(markerTitle);


                    }
                });








//        googleMap.setOnMapClickListener(pointLatLng -> {
//            MarkerOptions markerOptions = new MarkerOptions();
//            Double lat = pointLatLng.latitude; // 위도
//            Double lng = pointLatLng.longitude; // 경도
//            markerOptions.title("선택한 좌표");
//            markerOptions.position(new LatLng(lat, lng));
//            currentMarker = googleMap.addMarker(markerOptions);
//        });


    }

    private void setLocation(double lat, double lng, String title) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.title(title);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15);
        googleMap.moveCamera(cameraUpdate);
    }

    private void setDefaultLocation() {
        //디폴트 위치; Seoul
        LatLng DEFAULT_LOCATION_SEOUL = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION_SEOUL);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION_SEOUL, 15);
        googleMap.moveCamera(cameraUpdate);
    }

    /**
     * 정밀 위치 권한이 부여된 경우 내 위치 레이어를 활성화한다.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. 권한이 부여되었는지 확인하고 부여된 경우 내 위치 레이어를 활성화 한다.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            return;
        }
        // 2. 그렇지 않은 경우 사용자에게 위치 권한을 요청한다.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // 이벤트를 소비하지 않고 기본 동작이 계속 발생하도록 false를 반환
        // (카메라는 사용자의 현재 위치로 움직입니다).
        return false;
    }


    /**
     * @param requestCode  The request code passed in
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // 권한이 부여된 경우 내 위치 레이어를 활성화 한다.
            enableMyLocation();
        } else {
            // 허가가 거부되었습니다. (오류 메시지 표시)
            // [START_EXCLUDE]
            // 조각이 다시 시작될 때 누락된 권한 오류 대화 상자를 표시
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // 권한이 부여되지 않았습니다. (오류 대화 상자가 표시)
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * 위치 권한이 없음을 설명하는 오류 메시지가 있는 대화 상자
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}