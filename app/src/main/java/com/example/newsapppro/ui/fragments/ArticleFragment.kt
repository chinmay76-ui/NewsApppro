package com.example.newsapppro.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapppro.R
import com.example.newsapppro.databinding.FragmentArticleBinding
import com.example.newsapppro.ui.NewsViewModel
import com.example.newsapppro.ui.NewsActivity
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {  // ✅ FIXED NAME

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding

    private val args: ArticleFragmentArgs by navArgs() // ✅ this stays the same

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            newsViewModel.saveArticle(article)
            Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show()
        }
    }
}
