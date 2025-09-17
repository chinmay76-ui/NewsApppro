package com.example.newsapppro.models

data class NewsResponse(
    val status: String?,
    val totalResults: Int?,
    // Mutable so we can addAll(...) in ViewModel without crashes
    val articles: MutableList<Article>
)
