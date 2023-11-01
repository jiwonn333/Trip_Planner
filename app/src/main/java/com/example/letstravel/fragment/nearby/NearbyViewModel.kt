package com.example.letstravel.fragment.nearby

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NearbyViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    // 장소명
    private val _placeName = MutableStateFlow<List<PlaceRepository>>(emptyList())
    val placeName = _placeName.asStateFlow()

    // 주소
    private val _placeAddress = MutableStateFlow<List<PlaceRepository>>(emptyList())
    val placeAddress = _placeAddress.asStateFlow()

    // 이미지
    private val _placeBitmap = MutableStateFlow<List<PlaceRepository>>(emptyList())
    val placeBitmap = _placeBitmap.asStateFlow()


    fun searchPlaceName(placeName: PlaceRepository) {
        _placeName.update { listOf(placeName) }
    }

    fun searchPlaceAddress(placeAddress: PlaceRepository) {
        _placeAddress.update { listOf(placeAddress) }
    }

    fun searchPlaceBitmap(placeBitmap: PlaceRepository) {
        _placeBitmap.update { listOf(placeBitmap) }
    }


}