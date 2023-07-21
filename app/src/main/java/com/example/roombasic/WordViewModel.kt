package com.example.roombasic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WordViewModel(application: Application) :
    AndroidViewModel(application) {
    private var allWords: LiveData<List<Word>>
    var wordRepository: WordRepository = WordRepository(application)
    fun getAllWords(): LiveData<List<Word>> {
        return allWords
    }

    init {
        allWords = wordRepository.getAllWords()
    }

    fun addWord(vararg word: Word) {
        wordRepository.addWord(*word)
    }

    fun clearWords() {
        wordRepository.clearWords()
    }

    fun updateWord(word: Word) {
        wordRepository.updateWord(word)
    }

}