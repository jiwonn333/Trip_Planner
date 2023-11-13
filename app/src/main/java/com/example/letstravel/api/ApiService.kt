package com.example.letstravel.api

import com.example.letstravel.api.geo_model.ReverseGeoResponse
import com.example.letstravel.api.text_search_model.PlacesTextSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/maps/api/geocode/json")
    fun getReverseGeoAddress(
        @Query("latlng") latLng: String,
        @Query("language") language: String,
        @Query("key") key: String
    ): Call<ReverseGeoResponse?>

    @GET("/maps/api/place/textsearch/json")
    fun placesTextSearch(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("key") key: String
    ): Call<PlacesTextSearchResponse>

}