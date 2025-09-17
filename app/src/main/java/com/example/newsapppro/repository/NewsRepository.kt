package com.example.newsapppro.repository

import com.example.newsapppro.api.RetrofitInstance
import com.example.newsapppro.db.ArticleDatabase
import com.example.newsapppro.models.Article

class NewsRepository(
    private val db: ArticleDatabase
) {

    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)   // ✅ only 2 args now

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)     // ✅ only 2 args now

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun searchSavedNews(query: String) = db.getArticleDao().searchNews(query)
}
