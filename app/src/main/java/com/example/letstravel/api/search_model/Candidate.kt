package com.example.letstravel.api.search_model

data class Candidate(
    val formatted_address: String,
    val geometry: Geometry,
    val name: String,
    val opening_hours: OpeningHours,
    val rating: Double
)