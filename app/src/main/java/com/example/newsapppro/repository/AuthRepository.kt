package com.example.newsapppro.repository

import com.example.newsapppro.db.UserDao
import com.example.newsapppro.models.User

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(user: User): Boolean {
        return try {
            userDao.registerUser(user)
            true   // success
        } catch (e: Exception) {
            false  // failed
        }
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.loginUser(username, password)
    }
}
