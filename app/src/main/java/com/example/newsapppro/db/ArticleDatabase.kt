package com.example.newsapppro.db

import android.content.Context
import androidx.room.*
import com.example.newsapppro.models.Article
import com.example.newsapppro.models.User   // ✅ Import User

@Database(
    entities = [Article::class, User::class], // ✅ Add User here
    version = 2, // ⚠️ Increase version since schema changed
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDAO
    abstract fun getUserDao(): UserDao  // ✅ Add UserDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_db.db"
                )
                    .fallbackToDestructiveMigration() // ✅ Useful when changing schema
                    .build().also { instance = it }
            }
    }
}
