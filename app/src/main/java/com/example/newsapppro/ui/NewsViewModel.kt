package com.example.newsapppro.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapppro.models.NewsResponse
import com.example.newsapppro.models.Article
import com.example.newsapppro.repository.NewsRepository
import com.example.newsapppro.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    /** Headlines */
    private val _headlines = MutableLiveData<Resource<NewsResponse>>()
    val headlines: LiveData<Resource<NewsResponse>> get() = _headlines

    var headlinesPage = 1
    private var headlinesResponse: NewsResponse? = null
    private var lastCountry: String? = null

    /** Search News (API) */
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> get() = _searchNews

    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    private var lastSearchQuery: String? = null

    init {
        getHeadlines("us")
    }

    /** Fetch top headlines from API */
    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        _headlines.postValue(Resource.Loading())
        try {
            if (internetConnection(getApplication())) {
                if (lastCountry != countryCode) {
                    headlinesPage = 1
                    headlinesResponse = null
                    lastCountry = countryCode
                }

                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                _headlines.postValue(handleHeadlinesResponse(response))
            } else {
                _headlines.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _headlines.postValue(Resource.Error("Network Failure"))
                else -> _headlines.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    /** Search news from API */
    fun searchNews(query: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection(getApplication())) {
                if (lastSearchQuery != query) {
                    searchNewsPage = 1
                    searchNewsResponse = null
                    lastSearchQuery = query
                }

                val response = newsRepository.searchNews(query, searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                _searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNews.postValue(Resource.Error("Network Failure"))
                else -> _searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    /** Handle top headlines API response */
    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(errorMessage(response))
    }

    /** Handle search news API response */
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(errorMessage(response))
    }

    /** Extract error message from API */
    private fun <T> errorMessage(response: Response<T>): String {
        return try {
            response.errorBody()?.string() ?: response.message()
        } catch (e: Exception) {
            response.message()
        }
    }

    /** Favourites */
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getFavouriteNews() = newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    /** Search inside saved favourites (Room DB) */
    fun searchSavedNews(query: String) = newsRepository.searchSavedNews(query)

    /** Internet Connection */
    private fun internetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
