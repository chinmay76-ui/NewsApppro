package com.example.newsapppro.models

import java.io.Serializable

data class Source(
    val id: String?,
    val name: String?
) : Serializable {
    // Safe hashCode so navigation/backstack never NPEs
    override fun hashCode(): Int {
        return (id?.hashCode() ?: 0) * 31 + (name?.hashCode() ?: 0)
    }
}
