package com.example.newsapppro.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.newsapppro.R
import com.example.newsapppro.ui.LoginActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val btnSignOut = view.findViewById<Button>(R.id.btnSignOut)

        // ✅ Load username from SharedPreferences
        val prefs = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "User")

        tvUsername.text = "Namaste, $username \uD83D\uDE4F"

        // ✅ Sign out button
        btnSignOut.setOnClickListener {
            // Clear session
            prefs.edit().clear().apply()

            // Redirect to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
