package com.example.letstravel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.letstravel.util.PermissionUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends AppCompatActivity {

//    /**
//     * 위치 권한 요청 요청 코드
//     *
//     * @see #onRequestPermissionsResult(int, String[], int[])
//     */
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//
//    /**
//     * {@link#onRequestPermissionsResult(int, String[], int[])}에서 반환된 후
//     * 요청된 권한이 거부되었는지 여부를 나타내는 플래그
//     */
//    private boolean permissionDenied = false;
//
//    private GoogleMap map;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
//        // TODO: 내 위치 레이어를 활성화하기 전에 다음을 요청
//        // 사용자의 위치 권한
//        // 위치 권한 요청
//        map.setOnMyLocationButtonClickListener(this);
//        map.setOnMyLocationClickListener(this);
//        enableMyLocation();
//    }
//
//
//    /**
//     * 정밀 위치 권한이 부여된 경우 내 위치 레이어를 활성화한다.
//     */
//    @SuppressLint("MissingPermission")
//    private void enableMyLocation() {
//        // 1. 권한이 부여되었는지 확인하고 부여된 경우 내 위치 레이어를 활성화 한다.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
//            return;
//        }
//        // 2. 그렇지 않은 경우 사용자에게 위치 권한을 요청한다.
//        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
//    }
//
//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//        // 이벤트를 소비하지 않고 기본 동작이 계속 발생하도록 false를 반환합니다.
//        // (카메라는 사용자의 현재 위치로 움직입니다).
//        return false;
//    }
//
//
//    /**
//     * @param requestCode  The request code passed in
//     * @param permissions  The requested permissions. Never null.
//     * @param grantResults The grant results for the corresponding permissions
//     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
//     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            return;
//        }
//
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
//                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
//                .isPermissionGranted(permissions, grantResults,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            // 권한이 부여된 경우 내 위치 레이어를 활성화 한다.
//            enableMyLocation();
//        } else {
//            // 허가가 거부되었습니다. (오류 메시지 표시)
//            // [START_EXCLUDE]
//            // 조각이 다시 시작될 때 누락된 권한 오류 대화 상자를 표시
//            permissionDenied = true;
//            // [END_EXCLUDE]
//        }
//    }
//
//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        if (permissionDenied) {
//            // 권한이 부여되지 않았습니다. (오류 대화 상자가 표시)
//            showMissingPermissionError();
//            permissionDenied = false;
//        }
//    }
//
//
//    /**
//     * 위치 권한이 없음을 설명하는 오류 메시지가 있는 대화 상자
//     */
//    private void showMissingPermissionError() {
//        PermissionUtils.PermissionDeniedDialog
//                .newInstance(true).show(getSupportFragmentManager(), "dialog");
//    }
}