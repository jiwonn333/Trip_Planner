package com.example.letstravel.api.text_search_model

data class PlacesTextSearchResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)