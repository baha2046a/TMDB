package com.example.tmdb.model

data class Genre(
    val id: Int,
    val name: String
) {
    // Every Genre should have a unique ID
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Genre) return false

        return (id == other.id)
    }

    override fun hashCode(): Int {
        return id
    }
}