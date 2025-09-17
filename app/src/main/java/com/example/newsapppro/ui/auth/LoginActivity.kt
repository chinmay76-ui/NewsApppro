package com.example.newsapppro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newsapppro.R
import com.example.newsapppro.db.UserDatabase
import com.example.newsapppro.models.User
import com.example.newsapppro.repository.AuthRepository
import com.example.newsapppro.viewmodel.AuthViewModel
import com.example.newsapppro.viewmodel.AuthViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ If already logged in → go straight to NewsActivity
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val savedUsername = prefs.getString("username", null)
        if (savedUsername != null) {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // --- Init DAO, Repository, ViewModel ---
        val userDao = UserDatabase.getInstance(this).getUserDao()
        val repository = AuthRepository(userDao)
        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        // --- UI Elements ---
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // --- Register Button ---
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = User(username = username, password = password)
                viewModel.register(user)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe Register Result
        viewModel.registerResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Login Button ---
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe Login Result
        viewModel.loginResult.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Login Success: ${user.username}", Toast.LENGTH_SHORT).show()

                // ✅ Save username for ProfileFragment
                prefs.edit().putString("username", user.username).apply()

                // ✅ Navigate to NewsActivity
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
