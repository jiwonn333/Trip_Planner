package com.example.letstravel.api

import android.content.Context
import com.example.letstravel.api.model.ReverseGeoResponse
import com.example.letstravel.util.Constant
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/maps/api/geocode/json")
    fun getReverseGeoAddress(
        @Query("latlng") latlng: Double,
        @Query("key") key: String,
        @Query("language") language: String
    ): Call<ReverseGeoResponse?>


}