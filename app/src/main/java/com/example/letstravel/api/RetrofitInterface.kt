package com.example.letstravel.api

import retrofit2.Response

interface RetrofitInterface {
    fun onResponse(response: Response<*>?)

    fun onFailure(t: Throwable?)
}