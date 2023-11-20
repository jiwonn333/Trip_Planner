package com.example.letstravel.fragment.transport

import TransportRecyclerViewAdapter
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.BuildConfig
import com.example.letstravel.R
import com.example.letstravel.api.RetrofitApiManager
import com.example.letstravel.api.RetrofitInterface
import com.example.letstravel.api.geo_model.ReverseGeoResponse
import com.example.letstravel.databinding.FragmentTransportBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.util.CLog
import com.example.letstravel.util.PermissionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

class TransportFragment : BaseFragment() {
    private var binding: FragmentTransportBinding? = null
    private val permissionManager = PermissionManager()
    var locationManager: LocationManager? = null
    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //    val stateViewModel: SavedStateViewModel by viewModels() // viewmodel 선언
//    private lateinit var viewModel: TransportViewModel

    private var selectedIconPosition: Int = 0
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: TransportRecyclerViewAdapter? = null

    private val itemList: ArrayList<RecyclerViewDirectionItem> =
        object : ArrayList<RecyclerViewDirectionItem>() {
            init {
                add(RecyclerViewDirectionItem(R.drawable.ic_direction_bus))
                add(RecyclerViewDirectionItem(R.drawable.ic_direction_car))
                add(RecyclerViewDirectionItem(R.drawable.ic_direction_walk))
                add(RecyclerViewDirectionItem(R.drawable.ic_direction_bike))

            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransportBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        // permission 권한 설정 확인
        val locationPermissionGranted = permissionManager.isPermissionGranted(
            requireContext(), PermissionManager.AppPermission.FINE_LOCATION
        )

        getCurrentAddress(locationPermissionGranted)
        initRecyclerView()

        binding?.ibSwap?.setOnClickListener {
            var swap = binding?.tvStart?.text.toString()
            binding?.tvStart?.text = binding?.tvArrive?.text.toString()
            binding?.tvArrive?.text = swap
        }

        binding?.ibClose?.setOnClickListener {
            removeFragment(this)
        }


    }

    @SuppressLint("MissingPermission")
    private fun getCurrentAddress(locationPermissionGranted: Boolean) {
        var latLng: LatLng
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            val addOnCompleteListener = locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        requestService(
                            LatLng(
                                lastKnownLocation!!.latitude,
                                lastKnownLocation!!.longitude
                            )
                        )
                    }
                } else {
                    CLog.e("Exception: %s", task.exception)
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun requestService(latLng: LatLng) {
        RetrofitApiManager.getInstance()
            .requestReverseGeoAddress(latLngConcat(latLng.latitude, latLng.longitude),
                "ko",
                BuildConfig.MAPS_API_KEY,
                object : RetrofitInterface {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(response: Response<*>?) {
                        CLog.d("response : $response")

                        if (response != null) {
                            if (response.isSuccessful) {
                                CLog.d("response is Successful!!")
                                val reverseGeoResponse = response.body() as ReverseGeoResponse
                                val result = reverseGeoResponse.results
                                val formattedAddress = result[0].formatted_address
                                CLog.e("formatted_address : $formattedAddress")
                                binding?.tvStart?.text = "내위치: $formattedAddress"
                            }
                        }
                    }

                    override fun onFailure(t: Throwable?) {
                        CLog.e(t.toString())
                    }

                })
    }

//    onViewStateRestored??


    fun latLngConcat(lat: Double, lng: Double): String {
        return "$lat, $lng"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        recyclerView = binding?.directionRecyclerview
        recyclerViewAdapter = TransportRecyclerViewAdapter(requireContext(), itemList)

        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = gridLayoutManager

        recyclerView?.adapter = recyclerViewAdapter
        recyclerViewAdapter!!.addItem(itemList)
        recyclerViewAdapter?.notifyDataSetChanged()
    }
}

