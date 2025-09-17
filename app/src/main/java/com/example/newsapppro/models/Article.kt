package com.example.newsapppro.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    // NewsAPI fields can be null – make them nullable
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,      // nullable + Serializable
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable
