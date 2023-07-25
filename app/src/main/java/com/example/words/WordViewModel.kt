package com.example.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class WordViewModel(application: Application) :
    AndroidViewModel(application) {
    private var allWords: LiveData<List<Word>>
    private var wordRepository: WordRepository = WordRepository(application)
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

    fun updateWord(vararg word: Word) {
        wordRepository.updateWord(*word)
    }

    fun getSearchWord(key: String): LiveData<List<Word>> {
        return wordRepository.getSearchWord(key)
    }

    fun deleteSingleWord(vararg word: Word) {
        return wordRepository.deleteSingleWords(*word)
    }

}