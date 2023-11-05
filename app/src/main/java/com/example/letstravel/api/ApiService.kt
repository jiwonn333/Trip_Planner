package com.example.letstravel.api

import com.example.letstravel.api.geo_model.ReverseGeoResponse
import com.example.letstravel.api.search_model.PlacesFindPlaceFromTextResponse
import com.google.android.libraries.places.api.model.LocationBias
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/maps/api/geocode/json")
    fun getReverseGeoAddress(
        @Query("latlng") latLng: String,
        @Query("language") language: String,
        @Query("key") key: String
    ): Call<ReverseGeoResponse?>

    @GET("/maps/api/place/findplacefromtext/json")
    fun findPlaceFromText(
        @Query("input") input: String,
        @Query("inputtype") inputType: String,
        @Query("language") language: String,
//        @Query("locationbias") locationBias: LocationBias,
        @Query("key") key: String
    ): Call<PlacesFindPlaceFromTextResponse>

}