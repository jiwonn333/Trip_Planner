package com.example.letstravel.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.letstravel.BuildConfig
import com.example.letstravel.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class GoogleMapManager {
    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

        /**
         * 활동 상태를 저장하기 위한 키.
         */
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

        /**
         * 현재 위치를 선택하는 데 사용
         */
        private const val M_MAX_ENTRIES = 5
    }

    private val permissionManager = PermissionManager()
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * 위치 권한이 부여되지 않은 경우 사용할 기본 위치(서울) 및 기본 확대/축소
     */
    private var defaultLocation = LatLng(37.56, 126.97)

    /**
     * 장치가 현재 위치한 지리적 위치. 즉, Fused Location Provider에서 검색한 마지막으로 알려진 위치
     */
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)


    private lateinit var googleMap: GoogleMap

    fun setInitGoogleMap(googleMap: GoogleMap, context: Context) {
        this.googleMap = googleMap
        Places.initialize(context, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(context)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }


    /**
     * 사용자가 위치 권한을 부여했는지 여부에 따라 지도의 UI 설정을 업데이트 한다.
     */
    fun updateLocationUI(locationPermissionGranted: Boolean, googleMap: GoogleMap) {
        if (googleMap == null) {
            return
        }
        try {
            googleMap.isMyLocationEnabled = locationPermissionGranted
            googleMap.setPadding(0, 0, 0, 0)
            googleMap.uiSettings.isMyLocationButtonEnabled = locationPermissionGranted
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }

    // 현재 장소 선택하기
    fun getDeviceLocation(googleMap: GoogleMap, locationPermissionGranted: Boolean) {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.e("test", "Exception: %s", task.exception)
                        googleMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    fun moveCamera(latitude: Double, longitude: Double) {
        val latlng = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM.toFloat()))
    }

    fun makerClick(googleMap: GoogleMap, context: Context) {
        // 정보창 클릭리스너
        googleMap.setOnInfoWindowClickListener { marker ->
            val markerId = marker.id
            Toast.makeText(context, "정보창 클릭 Marker ID : " + markerId, Toast.LENGTH_SHORT).show()
        }
        // 마커 클릭 리스너
        googleMap.setOnMarkerClickListener { marker ->
            val markerId = marker.id
            // 선택한 타겟의 위치
            val location = marker.position
            Toast.makeText(
                context,
                "마커 클릭 Marker ID : " + markerId + "(" + location.latitude + " " + location.longitude + ")",
                Toast.LENGTH_SHORT
            ).show()
            return@setOnMarkerClickListener false
        }
    }

    fun saveInstanceState(outState: Bundle, googleMap: GoogleMap) {
        googleMap?.let { googleMap ->
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
    }

    @SuppressLint("MissingPermission")
    fun showCurrentPlace(locationPermissionGranted: Boolean, context: Context) {
        if (googleMap == null) {
            return
        }

        if (locationPermissionGranted) {
            //필드를 사용하여 반환할 데이터 유형을 정의
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // 빌더를 사용하여 FindCurrentPlaceRequest를 작성
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // 가능성이 높은 장소, 즉 기기의 현재 위치와 가장 잘 일치하는 업체 및 기타 관심 장소를 가져옴
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // 개수를 설정하고 반환되는 항목이 5개 미만인 경우를 처리
                    val count =
                        if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                            likelyPlaces.placeLikelihoods.size
                        } else {
                            M_MAX_ENTRIES
                        }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls(count)
                    likelyPlaceLatLngs =
                        arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // 사용자에게 보여줄 가능성이 있는 장소 목록 작성
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place
                            .attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // 사용자에게 가능한 장소 목록을 제공하는 대화 상자를 표시하고 선택한 장소에 마커를 추가
                    openPlacesDialog(context)
                } else {
                    Log.e("test", "Exception: %s", task.exception)
                }
            }
        } else {
            Log.i("test", "사용자가 권한을 부여하지 않았습니다.")

            // 사용자가 장소를 선택하지 않았으므로 기본 마커를 추가
            googleMap.addMarker(
                MarkerOptions()
                    .title(R.string.title_information.toString())
                    .position(defaultLocation)
                    .snippet(R.string.default_info_snippet.toString())
            )

            if (!locationPermissionGranted) {
                permissionManager.requestPermission(
                    context as Activity,
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
                    PermissionManager.AppPermission.FINE_LOCATION
                )
            }

        }

    }

    private fun openPlacesDialog(context: Context) {
        val listener =
            DialogInterface.OnClickListener { _, which -> // The "which" argument contains the position of the selected item.
                val markerLatLng = likelyPlaceLatLngs[which]
                var markerSnippet = likelyPlaceAddresses[which]
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
                }

                if (markerLatLng == null) {
                    return@OnClickListener
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                googleMap.addMarker(
                    MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet)
                )

                // Position the map's camera at the location of the marker.
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markerLatLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }

        // Display the dialog.
        AlertDialog.Builder(context)
            .setTitle(R.string.title_mypage)
            .setItems(likelyPlaceNames, listener)
            .show()
    }

}