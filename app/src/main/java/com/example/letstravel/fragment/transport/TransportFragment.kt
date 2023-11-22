package com.example.letstravel.fragment.transport

import TransportRecyclerViewAdapter
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.BuildConfig
import com.example.letstravel.R
import com.example.letstravel.api.RetrofitApiManager
import com.example.letstravel.api.RetrofitInterface
import com.example.letstravel.api.geo_model.ReverseGeoResponse
import com.example.letstravel.databinding.FragmentTransportBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.util.AppUtil
import com.example.letstravel.util.CLog
import com.example.letstravel.util.PermissionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response

class TransportFragment : BaseFragment(), View.OnClickListener {
    private var binding: FragmentTransportBinding? = null
    private val permissionManager = PermissionManager()
    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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

    private lateinit var transportViewModel: TransportViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransportBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // permission 권한 설정 확인
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationPermissionGranted = permissionManager.isPermissionGranted(
            requireContext(),
            PermissionManager.AppPermission.FINE_LOCATION
        )
        getCurrentAddress(locationPermissionGranted)
        initRecyclerView()
        initViewModel()

        binding?.ibClose?.setOnClickListener(this)
        binding?.ibSwap?.setOnClickListener(this)
        binding?.tvStart?.setOnClickListener(this)
        binding?.tvArrive?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.ibClose?.id -> removeFragment(this)
            binding?.ibSwap?.id -> swapAddress()
            binding?.tvStart?.id -> {
                transportViewModel.checkNum(1)
                transportViewModel.updateAddress(binding?.tvStart?.text.toString())
                replaceFragment()
            }
            binding?.tvArrive?.id -> {
                transportViewModel.checkNum(2)
                transportViewModel.updateAddress(binding?.tvArrive?.text.toString())
                replaceFragment()
            }
        }
    }

    private fun replaceFragment() {
        replaceFragment(
            this@TransportFragment,
            TransportFragmentDirections.actionNavigationTransportToNavigationTransportDetail()
        )
    }

    private fun checkEmpty(text: String, view: TextView) {
        if (text == "") {
            transportViewModel.updateAddress(view.hint.toString())
        } else {
            transportViewModel.updateAddress(text)
        }
    }

    private fun swapAddress() {
        var swap = binding?.tvStart?.text.toString()
        binding?.tvStart?.text = binding?.tvArrive?.text.toString()
        binding?.tvArrive?.text = swap
    }

    private fun initViewModel() {
        transportViewModel = ViewModelProvider(requireActivity()).get(TransportViewModel::class.java)
        transportViewModel.getCheckNum().observe(viewLifecycleOwner) { num ->
            when (num) {
                1 -> {
                    transportViewModel.updateAddress(binding?.tvStart?.text.toString())
                }
                2 -> {
                    transportViewModel.updateAddress(binding?.tvArrive?.text.toString())
                }
            }
        }


    }

    @SuppressLint("MissingPermission")
    private fun getCurrentAddress(locationPermissionGranted: Boolean) {
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
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
                            } else {
                                CLog.e("response is not successful")
                            }
                        } else {
                            CLog.e("response is null")
                        }
                    }

                    override fun onFailure(t: Throwable?) {
                        var errors = t.toString()
                        var wordError = errors.split(":")

                        when (wordError[0]) {
                            "java.net.UnknownHostException" -> {
                                AppUtil.showToast(context, "인터넷 연결 확인")
                            }
                            "java.net.SocketTimeoutException" -> {
                                AppUtil.showToast(context, "SocketTimeoutException")
                            }
                            else -> CLog.e(errors)
                        }
                    }

                })
    }

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

