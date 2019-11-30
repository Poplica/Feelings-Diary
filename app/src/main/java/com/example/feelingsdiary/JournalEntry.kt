package com.example.feelingsdiary

import java.io.Serializable

data class JournalEntry (
    private var date: String = "",
    private var entry: String = "",
    private var rating: String = "",
    private var tags: ArrayList<String> = arrayListOf()
    ) : Serializable {

    fun getDate(): String {
        return date
    }

    fun getEntry(): String {
        return entry
    }

    fun getRating(): String {
        return rating
    }

    fun getTags(): ArrayList<String> {
        return tags
    }
}
