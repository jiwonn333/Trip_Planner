package com.example.letstravel.api.search_model

data class PlacesFindPlaceFromTextResponse(
    val candidates: List<Candidate>,
    val status: String
)
