package com.example.letstravel

import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.letstravel.util.GoogleMapManager
import com.example.letstravel.util.PermissionManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivityKt : AppCompatActivity(), OnMapReadyCallback {
    private val googleMapManager = GoogleMapManager()
    private val permissionManager = PermissionManager()
    private lateinit var lastKnownLocation: Location
    private var cameraPosition: CameraPosition? = null

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kt)

        // 저장된 인스턴스 상태에서 위치 및 카메라 위치 검색
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        initialized()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            val locationPermissionGranted = permissionManager.isPermissionGranted(
                this,
                PermissionManager.AppPermission.FINE_LOCATION
            )
            googleMapManager.showCurrentPlace(locationPermissionGranted, this)
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapManager.setInitGoogleMap(googleMap, this)

        val locationPermissionGranted = permissionManager.isPermissionGranted(
            this,
            PermissionManager.AppPermission.FINE_LOCATION
        )

        // 권한 없을 경우 설정
        if (!locationPermissionGranted) {
            permissionManager.requestPermission(
                this,
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
                PermissionManager.AppPermission.FINE_LOCATION
            )
        }

        // 지도에서 내 위치 레이어 및 관련 컨트롤 활성화
        googleMapManager.updateLocationUI(locationPermissionGranted, googleMap)

        // 장치에 저장되어있는 현재 위치를 가져오고 지도에 표시
        googleMapManager.getDeviceLocation(googleMap, locationPermissionGranted)

        // 마커 클릭
        googleMapManager.makerClick(googleMap, this)
    }


    /**
     *  프래그먼트에 대한 핸들 가져오기 및 콜백 등록
     *  bottom navigation 설정
     */
    private fun initialized() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_container_test) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_test) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view_test)
        setupWithNavController(bottomNav, navController)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            if (navDestination.id == R.id.navigation_save_add || navDestination.id == R.id.navigation_save_detail || navDestination.id == R.id.navigation_mypage || navDestination.id == R.id.navigation_add_place) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
            if (navDestination.id == R.id.navigation_save_add || navDestination.id == R.id.navigation_mypage || navDestination.id == R.id.navigation_save_detail) {
                supportActionBar!!.hide()
            } else {
                supportActionBar!!.show()
            }
        }
    }
}