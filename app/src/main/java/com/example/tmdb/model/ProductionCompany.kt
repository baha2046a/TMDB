package com.example.tmdb.model

data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
) {
    // Every ProductionCompany should have a unique ID
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductionCompany) return false

        return (id == other.id)
    }

    override fun hashCode(): Int {
        return id
    }
}