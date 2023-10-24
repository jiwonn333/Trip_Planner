package com.example.letstravel.fragment.nearby

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.letstravel.R
import com.example.letstravel.databinding.FragmentNearbyBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.util.GoogleMapManager
import com.example.letstravel.util.MyLocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

class NearbyFragment : BaseFragment() {
    private var locationManager = MyLocationManager()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val googleMapManager = GoogleMapManager()
    private var binding: FragmentNearbyBinding? = null
    private lateinit var placesClient: PlacesClient
    private var isCheckedBookMarker = false
    lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialized()
        initBottomSheet()

        persistentBottomSheetEvent()


    }

    private fun initialized() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // 반환할 데이터 유형
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autocompleteFragment.setCountries("KR")
//        autocompleteFragment.setLocationBias()
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN)
        autocompleteFragment.setHint(getString(R.string.search_text))


        // 응답을 처리하도록 PlaceSelectionListener를 설정
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Log.e("test", "error : $status")
            }

            override fun onPlaceSelected(place: Place) {
                Log.i("test", "selected place : " + place.id + ", " + place.address)
                showSearchedPlace(place)
            }
        })
    }

    private fun initBottomSheet() {
        bottomSheet = from(binding!!.nearbyBottomSheet)

        if (bottomSheet.state == STATE_EXPANDED) {
            bottomSheet.state = STATE_HIDDEN
        }

        Log.e("state", "bottom sheet state init : ${bottomSheet.state}")
    }

    private fun showSearchedPlace(place: Place) {
        bottomSheet.state = STATE_EXPANDED
        Log.e("state", "bottom sheet state onPlaceSelected : ${bottomSheet.state}")
        placesClient = Places.createClient(requireContext())

        // 필드를 지정, 사진 요청에는 항상 PHOTO_METADATAS 필드가 있어야 한다.
        val fields = listOf(Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(place.id, fields)

        placesClient.fetchPlace(placeRequest).addOnSuccessListener { response: FetchPlaceResponse ->
            val responsePlace = response.place
            Log.e("test", "place: $responsePlace")

            // Get the photo metadata.
            val metadata = responsePlace.photoMetadatas

            if (metadata == null || metadata.isEmpty()) {
                Log.w("test", "No photo metadata.")
                return@addOnSuccessListener
            }

            val photoMetadata = metadata.first()

            // Get the attribution text.
            val attributions = photoMetadata?.attributions
            Log.e("test", "attribution: $attributions")

            // Create a FetchPhotoRequest.
            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build()

            placesClient.fetchPhoto(photoRequest)
                .addOnSuccessListener { photoResponse: FetchPhotoResponse ->


                    val bitmap = photoResponse.bitmap
                    binding?.ivImage?.setImageBitmap(bitmap)
                    binding?.tvTitle?.text = place.name
                    binding?.tvAddress?.text = place.address
                    Log.e("test", "name : ${place.name}, address: ${place.address}")
                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        Log.e("test", "Place not found: " + exception.message)
                        val statusCode = exception.statusCode
                        Log.e("test", "statusCode: $statusCode")
                    }
                }
        }
    }


    private fun persistentBottomSheetEvent() {
        bottomSheet.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_COLLAPSED -> {
                        Log.d("state", "onStateChanged: 접음")
                    }
                    STATE_DRAGGING -> {
                        Log.d("state", "onStateChanged: 드래그")
                    }
                    STATE_EXPANDED -> {
                        Log.d("state", "onStateChanged: 펼침")
                    }
                    STATE_HIDDEN -> {
                        Log.d("state", "onStateChanged: 숨기기")
                    }
                    STATE_SETTLING -> {
                        Log.d("state", "onStateChanged: 고정됨")
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("state", "onStateChanged: 드래그 중")
            }

        })
    }
}