package com.example.newsapppro.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapppro.models.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    // Local (favorites) search
    @Query("""
        SELECT * FROM articles 
        WHERE (title    LIKE '%' || :searchQuery || '%')
           OR (description LIKE '%' || :searchQuery || '%')
    """)
    fun searchNews(searchQuery: String): LiveData<List<Article>>
}
