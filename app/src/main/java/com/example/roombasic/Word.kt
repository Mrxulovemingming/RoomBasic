package com.example.roombasic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(var word: String, var chineseMeaning: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}