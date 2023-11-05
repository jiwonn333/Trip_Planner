package com.example.letstravel.api

import com.example.letstravel.api.geo_model.ReverseGeoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/maps/api/geocode/json")
    fun getReverseGeoAddress(
        @Query("latlng") latLng: String,
        @Query("language") language: String,
        @Query("key") key: String,
    ): Call<ReverseGeoResponse?>


}