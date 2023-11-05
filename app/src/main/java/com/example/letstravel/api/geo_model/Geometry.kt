package com.example.letstravel.api.geo_model

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)