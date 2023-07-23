package com.example.words

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(var word: String, var chineseMeaning: String,var showChinese: Boolean = true) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}