package com.example.letstravel.api

import android.content.Context
import com.example.letstravel.api.model.ReverseGeoResponse
import com.example.letstravel.util.Constant
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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