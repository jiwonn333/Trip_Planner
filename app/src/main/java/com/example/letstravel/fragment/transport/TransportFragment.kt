package com.example.letstravel.fragment.transport

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.letstravel.BuildConfig
import com.example.letstravel.R
import com.example.letstravel.api.RetrofitApiManager
import com.example.letstravel.api.RetrofitInterface
import com.example.letstravel.api.model.ReverseGeoResponse
import com.example.letstravel.databinding.FragmentTransportBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.fragment.common.SavedStateViewModel
import com.example.letstravel.util.CLog
import com.example.letstravel.util.PermissionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

class TransportFragment : BaseFragment() {
    private var binding: FragmentTransportBinding? = null
    private val permissionManager = PermissionManager()
    var locationManager: LocationManager? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val stateViewModel: SavedStateViewModel by viewModels() // viewmodel 선언

    companion object {
        fun newInstance() = TransportFragment()
        private const val KEY_LOCATION = "location"
    }

    private lateinit var viewModel: TransportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransportBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        // permission 권한 설정 확인
        val locationPermissionGranted = permissionManager.isPermissionGranted(
            requireContext(),
            PermissionManager.AppPermission.FINE_LOCATION
        )
        // 0. 출발지 입력 부분 >> 현재 위치 latlng 가져오기
        test(locationPermissionGranted)
        // 1. 현재 사용자 위치 불러온 후 start에 주소 찍어주기
        // 2.  start/arrive 주소의 latlng 가져온 후 setting

    }

    @SuppressLint("MissingPermission")
    private fun test(locationPermissionGranted: Boolean) {
        if (locationPermissionGranted) {
            var locationLatLng = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var latLng = LatLng(locationLatLng!!.latitude, locationLatLng.longitude)

            // 역 지오코딩을 사용해 위도/경도 -> 주소로 표현하기 / 통신
            requestService(latLng)
        }

    }

    private fun requestService(latLng: LatLng) {
        RetrofitApiManager.getInstance()
            .requestReverseGeoAddress(concat(latLng.latitude, latLng.longitude), "ko", BuildConfig.MAPS_API_KEY, object : RetrofitInterface {
                @SuppressLint("SetTextI18n")
                override fun onResponse(response: Response<*>?) {
                    CLog.d("response : $response")

                    if (response != null) {
                        if (response.isSuccessful) {
                            CLog.d("response is Successful!!")
                            var reverseGeoResponse = response.body() as ReverseGeoResponse
                            if (reverseGeoResponse != null) {
                                var result = reverseGeoResponse.results
                                if (result != null) {
                                    var name = result[2].formatted_address
                                    if (name != null) {
                                        CLog.e("name : $name")
                                        binding?.tvStart?.text = "내위치: $name"
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable?) {
                    CLog.e(t.toString())
                }

            })
    }

//    onViewStateRestored??


    fun concat(lat: Double, lng: Double): String {
        return "$lat, $lng"
    }
}
