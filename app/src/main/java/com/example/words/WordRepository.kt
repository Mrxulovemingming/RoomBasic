package com.example.words

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordRepository(context: Context) {
    private lateinit var allWords: LiveData<List<Word>>
    private lateinit var wordDao: WordDao

    init {
        var wordDatabase = WordDatabase.getWordDataBase(context.applicationContext)
        wordDao = wordDatabase.getWordDao()
        allWords = wordDao.getAllWords()
    }

    fun getAllWords(): LiveData<List<Word>> {
        return allWords
    }

    fun addWord(vararg word: Word) {
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.insertWords(*word)
        }
    }

    fun clearWords() {
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.deleteAllWords()
        }
    }

    fun updateWord(word: Word) {
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.updateWords(word)
        }
    }

    fun getSearchWord(key: String): LiveData<List<Word>> {
        return wordDao.getSearchWords("%$key%")
    }

}