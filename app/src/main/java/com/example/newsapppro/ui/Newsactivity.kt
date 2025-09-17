package com.example.newsapppro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapppro.R
import com.example.newsapppro.databinding.ActivityNewsBinding
import com.example.newsapppro.repository.NewsRepository
import com.example.newsapppro.db.ArticleDatabase

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ initialize DB properly
        val database = ArticleDatabase.getInstance(this)
        val repository = NewsRepository(database)

        // ✅ create ViewModel with factory
        val factory = NewsViewModelProviderFactory(application, repository)
        newsViewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        // ✅ setup navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
