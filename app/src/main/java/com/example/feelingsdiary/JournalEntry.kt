package com.example.feelingsdiary

import java.io.Serializable

//data class JournalEntry (val userID: String = "",
//                         val journalEntries: ArrayList<String> = arrayListOf())
data class JournalEntry (var date: String = "",
                         var entry: String = "",
                         var rating: String = "",
                         var tags: ArrayList<String> = arrayListOf()) : Serializable